import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.Enrollee;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EnrollmentParser {

    public static void main(String[] args) throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(args[0]));
        ) {
            CsvToBean<Enrollee> csvToBean = new CsvToBeanBuilder<Enrollee>(reader)
                    .withType(Enrollee.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            HashMap<String, HashMap<String, Enrollee>> enrollmentMap = new HashMap<>();
            Iterator<Enrollee> enrolleeIterator = csvToBean.iterator();

            while (enrolleeIterator.hasNext()) {
                Enrollee enrollee = enrolleeIterator.next();

                if (!enrollmentMap.containsKey(enrollee.getInsuranceCompany())) {
                    // Create new hashmap if we encounter a new insurance company
                    HashMap<String, Enrollee> insuranceCompanyMap = new HashMap<>();
                    insuranceCompanyMap.put(enrollee.getUserId(), enrollee);
                    enrollmentMap.put(enrollee.getInsuranceCompany(), insuranceCompanyMap);
                } else {
                    HashMap<String, Enrollee> insuranceCompanyMap = enrollmentMap.get(enrollee.getInsuranceCompany());
                    if (insuranceCompanyMap.containsKey(enrollee.getUserId()) && insuranceCompanyMap.get(enrollee.getUserId()).getVersion() > enrollee.getVersion()) {
                        // skip updating the map if the enrollee has already been processed and processed version is higher than current version
                        continue;
                    }
                    insuranceCompanyMap.put(enrollee.getUserId(), enrollee);
                }
            }

            // for each unique insurance company, sort the enrollees
            for (Map.Entry<String, HashMap<String, Enrollee>> entry : enrollmentMap.entrySet()) {
                HashMap<String, Enrollee> enrollees = entry.getValue();
                List<Enrollee> enrolleeList = new ArrayList<>(enrollees.values());
                Collections.sort(enrolleeList, Comparator.comparing(Enrollee::getLastName)
                    .thenComparing(Enrollee::getFirstName));

                // finally, save enrollees to their corresponding insuranceCompany file
                String insuranceCompany = entry.getKey();
                Writer writer = Files.newBufferedWriter(Paths.get(String.format("%s.csv", insuranceCompany)));

                StatefulBeanToCsv<Enrollee> csvWriter = new StatefulBeanToCsvBuilder<Enrollee>(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .withOrderedResults(true)
                    .build();

                csvWriter.write(enrolleeList);

                writer.close();
            }

        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }
}
