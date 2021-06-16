# Availity Enrollment CSV Separator

**Tools:**
- Java 8
- Gradle
- [OpenCSV](http://opencsv.sourceforge.net/)

**Features:**
- Processes a .csv file (with headers) of insurance enrolees, and creates new .csv files for each insurance provider.
- Sorts the enrollees by last/first name
- Filters out any duplicate enrollees (only saving the duplicate with the highest `version` property)

# Usage

**Build/compile:** `gradlew build`

**Run the application:** `gradlew run --args='./src/test/resources/testData/enrollees.csv'`

The first argument is the location of the input file. A [sample input file](https://github.com/ncerice/availity-csv-separator/blob/main/src/test/resources/testData/enrollees.csv) has been uploaded to this repo for testing purposes.

After running the application, new .csv files will be created in the working directory with the unique insurance company names as the filenames.
