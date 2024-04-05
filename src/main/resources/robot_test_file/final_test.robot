Invalid-Test-Login-1
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	data_ip1
	Input Text	loc_ip2	data_ip2
	Location Should Be	url5
	Close Browser
Invalid-Test-Login-2
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	INVALID_DATA
	Input Text	loc_ip2	INVALID_DATA
	Location Should Be	url1
LINE10	la5
	Close Browser
Invalid-Test-Login-3
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	data_ip1
	Input Text	loc_ip2	data_ip2
	Location Should Be	url1
	Input Text	loc_ip3	INVALID_DATA
	Input Text	loc_ip4	INVALID_DATA
	Location Should Be	url2
LINE10	la5
	Close Browser
Invalid-Test-Login-4
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	data_ip1
	Input Text	loc_ip2	data_ip2
	Location Should Be	url1
	Input Text	loc_ip3	data_ip3
	Input Text	loc_ip4	data_ip4
	Location Should Be	url2
	Input Text	loc_ip5	INVALID_DATA
	Input Text	loc_ip6	INVALID_DATA
	Input Text	loc_ip7	INVALID_DATA
	Location Should Be	url3
LINE10	la5
	Close Browser
Invalid-Test-Login-5
	Open Browser	link_url	Chrome
	Input Text	loc_ip1	data_ip1
	Input Text	loc_ip2	data_ip2
	Location Should Be	url1
	Input Text	loc_ip3	data_ip3
	Input Text	loc_ip4	data_ip4
	Location Should Be	url2
	Input Text	loc_ip5	data_ip5
	Input Text	loc_ip6	data_ip6
	Input Text	loc_ip7	data_ip7
	Location Should Be	url3
	Input Text	loc_ip8	INVALID_DATA
	Input Text	loc_ip9	INVALID_DATA
	Location Should Be	url4
LINE10	la5
	Close Browser
