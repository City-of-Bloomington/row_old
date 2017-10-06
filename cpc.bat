@echo off
set var=permit
copy .\build\WEB-INF\classes\%var%\*.class .\WEB-INf\classes\%var%\.
:done
