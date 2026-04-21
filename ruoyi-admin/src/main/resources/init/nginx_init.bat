echo off
setlocal enabledelayedexpansion

taskkill /f /t /im nginx.exe
cd C:\peizi-2.0\environment\nginx-1.20.2
start nginx.exe