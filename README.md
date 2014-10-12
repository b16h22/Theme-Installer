Theme-Installer
===============

Multiple theme installer for cm11 themes

* Adding themes
Put your themes inside assets/files/
Their names should be theme1.apk,theme2.apk,theme3.apk and theme4.apk respectively

* In the /res/values/arrays.xml you will find three arrays. There you have to add your Themes Names, Packages and Colors.

* Add theme icons
You can find 4 icons(theme_icon1, theme_icon2, ...) inside drawable directories. Replace them with your own icons. Dont change the names

* Add previews
There are four placeholder preview images (theme_preview1, theme_preview2, ...) inside drawable directories. Replace them with your theme previews

* Add values to strings
    <string name="current_version">1.0</string> // add your theme version( mandatory)
    <string name="app_name">Theme Installer</string> // change this to change app name

    <string name="share_link">share_link_here</string> // change this to a link you'd like users to share
	
    <string name="email_subject">Email Subject</string> // change this to the email subject you want emails from users to have
    <string name="email_address">someone@gmail.com</string> // change this to your email address

    <string name="rate_link">rate_link_here</string> // change this to a play store app you'd like to redirect users

* If you have less number of themes make sure to set the layout_weight value of unwanted layouts in activity_main.xml to "0.0"

* Colors
Every color used in the app including window background, action bar are coded here. Change them to change the visual properties 
