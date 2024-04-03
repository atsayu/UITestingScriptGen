Invalid-Test-Test Click-1
	Open Browser	link_url	Chrome
#	LINE1	ce1
#	LINE2	ce2
	Location Should Be	url1
	Close Browser
Invalid-Test-Test Click-2
	Open Browser	link_url	Chrome
	Click Element	val_cl1
	Click Element	val_cl2
	Location Should Be	url1
#	LINE4	ce3
	Location Should Be	url2
	Close Browser
Invalid-Test-Test Click-3
	Open Browser	link_url	Chrome
	Click Element	val_cl1
	Click Element	val_cl2
	Location Should Be	url1
	Click Element	val_cl3
	Location Should Be	url2
#	LINE6	ce4
#	LINE7	ce5
	Location Should Be	url3
	Close Browser
