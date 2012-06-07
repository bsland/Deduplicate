Deduplicate.java
================

Deduplicate.java looks for misspellings in a database column.

It compares all distinct entries and their counts. If 2 entries are within a
certain Levenshtein distance of each other (determined by the THRESHOLD
variable), it assumes that the more common spelling is the correct one.

Its plain text output groups such misspellings, with the most used spelling
first. This output can easily be converted into SQL UPDATE statements using
regular expressions after manual inspection.

<i>Note:</i> This program is still very specific to the project I am using
it for. I have plans to make it more generic, and possibly a plugin to a
larger project, in the near future.

Additionally, sorry to make you download the sqlite JDBC driver. I need it
for my project and therefore do not want to delete it.
