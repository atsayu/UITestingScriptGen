Invalid-Test-Only and 3-1
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	data_ip1
	Input Text	loc_ip2	data_ip2
#	LINE10	ce3
#	LINE11	ce4
	Location Should Be	url3
	Close Browser
Invalid-Test-Only and 3-2
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	INVALID_DATA
	Input Text	loc_ip2	INVALID_DATA
	Input Text	loc_ip3	INVALID_DATA
#	LINE3	ce1
	Input Text	loc_ip4	INVALID_DATA
	Location Should Be	url1
LINE10	ce3
LINE11	ce4
LINE12	la3
	Close Browser
Invalid-Test-Only and 3-3
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	data_ip1
	Input Text	loc_ip2	data_ip2
	Input Text	loc_ip3	data_ip3
	Click Element	loc_cl1
	Input Text	loc_ip4	data_ip4
	Location Should Be	url1
	Input Text	loc_ip5	INVALID_DATA
	Input Text	loc_ip6	INVALID_DATA
#	LINE7	ce2
	Location Should Be	url2
LINE10	ce3
LINE11	ce4
LINE12	la3
	Close Browser
