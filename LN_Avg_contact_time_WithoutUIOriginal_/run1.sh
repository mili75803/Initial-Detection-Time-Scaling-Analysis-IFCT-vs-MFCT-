#!/bin/bash

LNlength=(1.992110095   2.924017738     4.291871095     6.299605249     9.246555971)
TotalTC=(20     63	199     629     1989)
TotalDC=(80     253     801     2533    8011)
fileNum=(1	2	3	4	5)
ContactTimeType=0
fileNum=124
JOB_ID=5

for ((j=0;j<1;j++));
do
  	for ((i=1;i<=1;i++));
        do
	   NUM_STEPS="${fileNum[j]}${JOB_ID}"
	    java -jar "LN_first_contact_time_WithoutUIOriginal.jar" ${LNlength[j]} ${TotalTC[j]} ${TotalDC[j]} ${NUM_STEPS} ${ContactTimeType}
	done
done


