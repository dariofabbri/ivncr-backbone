psql -U postgres -f create_database.sql postgres


for sqlscript in `find . -regex ".*[0-9][0-9][0-9].*\.sql" | sort`
do

  psql -U ivncr -f $sqlscript ivncr

done

