Test-login
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT123
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT412
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
   Location Should Be   fadsf
   Input Text   locdata   NOTa1
   Location Should Be   urldata
