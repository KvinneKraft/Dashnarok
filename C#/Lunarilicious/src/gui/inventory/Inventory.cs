﻿
// Author: Dashie
// Version: 1.0

using System;
using System.IO;
using System.Text;
using System.Media;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;
using System.Collections.Generic;

namespace Lunarilicious
{
    public static class Inventory
    {
	public static void Show(bool hideAll)
	{
	    if (hideAll)
	    {
		if (StartMenu.START_MENU.Visible) StartMenu.START_MENU.Visible = false;
	    };

	    try
	    {

	    }

	    catch { };
	}

	public static void Setup()
	{

	}
    };
};