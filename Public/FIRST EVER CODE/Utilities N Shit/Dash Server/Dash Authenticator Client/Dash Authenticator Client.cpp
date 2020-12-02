
/*                                                */
/* (c) All Rights Reserved, Dashies Software Inc. */
/*                                                */
/*       Build : 1 | Version : Stable N Free      */
/*                                                */

#include "pch.h"

#include<windows.h>
#include<wininet.h>
#include<lmcons.h>
#include<conio.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<tchar.h>
#include<time.h>
#include<math.h>

#include<iostream>
#include<sstream>
#include<fstream>
#include<string>

#include "DashCore\\Dash Library.h"

using namespace std;
using namespace Server_Authentication;

struct Authentication_Table {
	std::string Arg, Username, Password, HelpMessage;
	bool Get, Bypass, Toggle_Special;
} AuthTable;

int main(int Length, char ** Argument) {
	if (Length <= 1) {
		DashieSays(2, Message_Table[0]);
		return 0;
	}

	// The reason we generate walls is to make the application we are building look more efficient, it puts you in a better perspective when it comes to the
	// Layout of my magical authentication program. GenerateWall() just simplifies the entire idea of multiple characters, the function will meet extension in
	// the Future for sure, as I am still planning and still working on improvements for the thing itself ^-^

	AuthTable.HelpMessage = {(
		GenerateWall(sizeof(id1), 1) + "\n\n" +
		"(c) All Rights Reserved, Dashies Software Inc. \n\n" +
		
		// I should really add in like an option for the user to save all of the shitty text bellow to a fucking file, because else it will be too much to read.
		// Seems proper, does it not? I dunno lol .-.

		GenerateWall(sizeof(id1)/2, 1) + " Authentication " + GenerateWall(sizeof(id1)/2, 1) + "\n\n" +
        
		// --login for authentication process server
		"--login <username> <password> <extra>" + "\n\n" +

		"this is a main command, it will allow you to login to our server and authenticate with" + "\n" +
		"the communication protocols we have. this server uses a custom algorithm which generates the" + "\n" +
		"hash keys for the authentication process. even tho we have tested these features, there still" + "\n" +
		"may be some issues that we did not see, if you do find any of them let us know." + "\n\n" +

		"      - <username> this is what you may change to your username." + "\n" +
		"      - <password> this is what you may change to your password." + "\n" +
		"      - <extra> this is what you may change to an extra option." + "\n\n" +

		"the options like the ones shown bellow can only be applied at the end of your main command." + "\n\n\n\n" +


		// toggle-special for --login
		"<main command> /toggle-special" + "\n\n" +

		"this is an extra command, this command may be applied to the end of the main command specified." + "\n" +
		"this command will enhance the \"--login\" authentication process, this command only applies to" + "\n" +
		"the following command \"--login\" as it has not been implemented to anything else yet!" + "\n\n" +

		"      - <main command> this is what you may change to \"--login\"." + "\n\n" + 

		"even though we have widely tested this command, we are still not sure if it is compatible with" + "\n" +
		"your operating systems, if you find any issues, please then report them ^-^" + "\n\n\n\n" +


		// --logout for --logout, kill authenticated session
		"--logout" + "\n\n" +

		"this is a main command, it will log you out of your current session and destroy any hashes" + "\n" + 
		"associated with it, traces will be removed and stacks will be emptied. please know that when you" + "\n" +
		"use this option the server will be terminated and all unsaved data will be lost, please be careful." + "\n\n" +

		GenerateWall(sizeof(id1)/2, 1) + " Tools " + GenerateWall(sizeof(id1)/2, 1) + "\n\n" +

		// --update for updating the application runtime version
		"--update" + "\n\n" +
		
		"this is a main command, like the command suggests, it will download and install the newest version" + "\n" +
 		"of Dash Server from one of our servers, this may be useful if you find any issues and you feel like" + "\n" + 
		"debugging them or if you just want to run the newest version. now thinking about it, i may add a command" + "\n\n" +
		"that will allow users like you to simply check the current and the new version of Dash Server." + "\n\n" +

		GenerateWall(sizeof(id1)/2, 1) + " Others " + GenerateWall(sizeof(id1)/2, 1) + "\n\n" +

		"5" + "\n" +
		"6" + "\n\n" +

		GenerateWall(sizeof(id1), 1)
    )};

	// The difference between std::cout and DashieSays() is that DashieSays() contains some standard functionality sets which I may use from time to time.
	// In the end I just added it to represent the name you know? ^-^

	// I should really think about adding somthing like information functionality, you know allowing the users to find out more about the application its featuressss

	for(int index = 1; index <= Length-1; index = index + 1) {
		AuthTable.Arg = Argument[index];
		AuthTable.Arg = ToLawerCase(AuthTable.Arg);
		AuthTable.Bypass = true;

		if(AuthTable.Arg == "--help") { 
			std::cout << AuthTable.HelpMessage << std::endl;
			return 0;
		} else
		if(AuthTable.Arg == "--update") {
			return 0;
		} else
		if(AuthTable.Arg == "--login") {
			if(Length < 4) {
				DashieSays(2, "insufficient or invalid input received for \"--login\", please try again.");
				return 0;
			} else {
			   AuthTable.Arg = Argument[2];
			   AuthTable.Username = AuthTable.Arg;

			    if((AuthTable.Username.length() < 4) || (AuthTable.Username.length() > 24)) {
					DashieSays(2, "too many or too less characters received for your username, you must atleast enter somewhere between 4 and 24 characters for it to be sufficient, please try again.");
					return 0;
				}

			   AuthTable.Arg = Argument[3];
			   AuthTable.Password = AuthTable.Arg;

			    if((AuthTable.Password.length() < 8) || (AuthTable.Password.length() > 32)) {
					DashieSays(2, "too many or too less characters received for your password, you must atleast enter somewhere between 8 and 32 characters for it to be sufficient, please try again.");
					return 0;
				}

			    if(Length > 4) {
					AuthTable.Arg = Argument[4];
				    if(AuthTable.Arg == "/toggle-special") AuthTable.Toggle_Special = true;
					else AuthTable.Toggle_Special = false;
				}

				// We will use a separate if statement for the toggle boolean because I feel like I will use it more often than yet seen.

				if(AuthTable.Toggle_Special == true) AuthTable.Get = AuthenticateUser(AuthTable.Username, AuthTable.Password, true);
				else AuthTable.Get = AuthenticateUser(AuthTable.Username, AuthTable.Password, false);

				if(AuthTable.Get == true) {
				    if(DoesServerExist() == true) {
						DashieSays(1, "Booting the Dash Server 1.0 up ....");

					    if(BootServer() == true) DashieSays(1, "Congratulations " + AuthTable.Username + " Server is up and running ^-^");
						else DashieSays(2, "Oops, It seems like something went wrong, unfortunately we were unable to boot up Dash Server 1.0 ;(");

						// Think about adding a listener here, that when the user types "--logout" for example, it allows them to logout.
					} else {
						DashieSays(2, "incorrect return value received, please try again.");
						DashieSays(2, "the booting process has been aborted!");
					}

					return 0;
				} else {
					DashieSays(2, "incorrect credentials received, please try again.");
					DashieSays(2, "the authentication process has been aborted!");

					return 0;
				}
		    }
		} else
		if(AuthTable.Arg == "--logout") {

			//
			


			//

			std::cout << "<> we are now checking for required files ...." << std::endl;

			if(DoesServerExist() != true) {
				DashieSays(2, "the logout process has been fully aborted.");
				return 0;
			}

			std::cout << "<> we successfully found all necessary files." << std::endl;
			
			if(IsProcessAlive("Dash Server.exe") != true) {
				DashieSays(2, "it seems like the server (dash server.exe) is not running at the moment, this application should be in the background in order for us to log you out safely. consider clicking the file manually and trying again.");
				DashieSays(2, "the logout process has been fully aborted.");
				return 0;
			} else {
				std::cout << "<> we are now terminating \"dash server.exe\" since it is running ...." << std::endl;

				if(Terminate_Server() != true) {
					std::cout << "<> we failed to terminate \"dash server.exe\" for some reason." << std::endl;
					std::cout << "<> the logout process has been fully aborted." << std::endl;
					return 0;
				} else {
					std::cout << "<> successfully terminated \"dash server.exe\" !" << std::endl;
				}
			}
			
			std::cout << "<> we are now destroying all the authentication data with in this runtime ...." << std::endl;

			if(DestroyAuthenticationData() != true) {
				DashieSays(2, "the logout process has been fully aborted.");
				return 0;
			}

			std::cout << "<> we have successfully destroyed all authentication data." << std::endl;
			std::cout << "<> you have successfully logged out, congratulations!" << std::endl;

			return 0;
		} else AuthTable.Bypass = false;
	}

	if(AuthTable.Bypass != true) {
		DashieSays(2, Message_Table[1]);
		return 0;
	}
}