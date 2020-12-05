﻿using System;
using System.Net;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;
using System.Collections.Generic;

namespace ThaDasher
{
    public class PortScanner : Form
    {
	readonly static DashControls CONTROL = new DashControls();
	readonly static DashTools TOOL = new DashTools();

	private void SendError(string m)
	{
	    MessageBox.Show($"It appears that an error has occurred.\r\n\r\nThe error message: \r\n{m}\r\n\r\nYou can send this to me at KvinneKraft@protonmail.com if you want to fix this.\r\n\r\nFor now you can click OK and restart the application.", "Oh...Ow", MessageBoxButtons.OK, MessageBoxIcon.Error);
	    Close();
	}
	public class GUI
	{
	    readonly public static PictureBox BRAND = new PictureBox();
	    readonly public static PictureBox BAR = new PictureBox();

	    readonly public static Button CLOSE = new Button();

	    readonly public static Label TITLE = new Label();

	    public static void InitializeGUI(Form TOP)
	    {
		TOP.BackColor = Color.MidnightBlue;
		TOP.Icon = Properties.Resources.ICON_ICO;

		TOP.FormBorderStyle = FormBorderStyle.None;

		try
		{
		    var BAR_SIZE = new Size(TOP.Width, 24);
		    var BAR_LOCA = new Point(0, 0);
		    var BAR_COLA = Color.FromArgb(12, 12, 12);

		    CONTROL.Image(TOP, BAR, BAR_SIZE, BAR_LOCA, null, BAR_COLA);

		    var BRAND_IMAG = Properties.Resources.ICON_PNG1;
		    var BRAND_SIZE = BRAND_IMAG.Size;
		    var BRAND_LOCA = new Point(5, 0);
		    var BRAND_COLA = BAR_COLA;

		    CONTROL.Image(BAR, BRAND, BRAND_SIZE, BRAND_LOCA, BRAND_IMAG, BRAND_COLA);

		    var TITLE_TEXT = "Dash Port Scanner";
		    var TITLE_SIZE = TOOL.GetFontSize(TITLE_TEXT, 10);
		    var TITLE_LOCA = TOOL.GetCenter(TOP, TITLE, new Point((BAR.Width - TITLE_SIZE.Width) / 2, (BAR.Height - TITLE_SIZE.Height) / 2));
		    var TITLE_BCOL = BAR_COLA;
		    var TITLE_FCOL = Color.White;

		    CONTROL.Label(BAR, TITLE, TITLE_SIZE, TITLE_LOCA, TITLE_BCOL, TITLE_FCOL, 1, 10, TITLE_TEXT);

		    var CLOSE_SIZE = new Size(55, BAR_SIZE.Height);
		    var CLOSE_LOCA = new Point(BAR_SIZE.Width - CLOSE_SIZE.Width, 0);
		    var CLOSE_BCOL = BAR_COLA;
		    var CLOSE_FCOL = Color.White;

		    CONTROL.Button(BAR, CLOSE, CLOSE_SIZE, CLOSE_LOCA, CLOSE_BCOL, CLOSE_FCOL, 1, 12, "X", Color.Empty);

		    CLOSE.Click += (s, e) =>
			TOP.Close();
		   
		    var RECT_SIZE = new Size(TOP.Width, TOP.Height - BAR_SIZE.Height + 1);
		    var RECT_LOCA = new Point(0, BAR_SIZE.Height - 2);
		    var RECT_COLA = BAR_COLA;

		    TOOL.PaintRectangle(TOP, 4, RECT_SIZE, RECT_LOCA, RECT_COLA);

		    foreach (Control CON in TOP.Controls)
			TOOL.Interactive(CON, TOP);

		    foreach (Control CON in BAR.Controls)
			TOOL.Interactive(CON, TOP);
		    
		    TOOL.Interactive(TOP, TOP);
		}

		catch
		{
		    throw new Exception("InitializeGUI()");
		};

		TOOL.Round(TOP, 6);
	    }
	}

	public class OPS
	{
	    readonly static RichTextBox PORT = new RichTextBox();
	    readonly static RichTextBox LOGS = new RichTextBox();

	    readonly static PictureBox PORTCONTAINER = new PictureBox();
	    readonly static PictureBox LOGSCONTAINER = new PictureBox();
	    readonly static PictureBox CONTAINER = new PictureBox();

	    readonly static Button YES = new Button() { Enabled = false, Visible = false };
	    readonly static Button NUU = new Button() { Enabled = false, Visible = false };
	    readonly static Button TOGGLE = new Button();

	    public static void InitializeContainer(Form TOP)
	    {
		var TOGGLE_SIZE = new Size(145, 28);
		var TOGGLE_LOCA = new Point((TOP.Width - TOGGLE_SIZE.Width) / 2, GUI.BAR.Height + 6);
		var TOGGLE_BCOL = LogContainer.CLEAR.BackColor;  Color.FromArgb(12, 12, 12);
		var TOGGLE_FCOL = Color.White;

		CONTROL.Button(TOP, TOGGLE, TOGGLE_SIZE, TOGGLE_LOCA, TOGGLE_BCOL, TOGGLE_FCOL, 1, 12, "Start Scan", Color.Empty);
		TOOL.Round(TOGGLE, 6);
	    
		var y = GUI.BAR.Height + 10 + TOGGLE.Height + 2;

		var PORTCONTAINER_SIZE = new Size(TOP.Width - 20, 80);
		var PORTCONTAINER_LOCA = new Point(10, y);
		var PORTCONTAINER_BCOL = LogContainer.LOG.BackColor;

		CONTROL.Image(TOP, PORTCONTAINER, PORTCONTAINER_SIZE, PORTCONTAINER_LOCA, null, PORTCONTAINER_BCOL);
		TOOL.Round(PORTCONTAINER, 6);

		var PORT_SIZE = new Size(PORTCONTAINER.Width - 10, PORTCONTAINER.Height - 10);
		var PORT_LOCA = new Point(5, 5);
		var PORT_BCOL = LogContainer.LOG.BackColor;
		var PORT_FCOL = Color.White;
		
		CONTROL.RichTextBox(PORTCONTAINER, PORT, PORT_SIZE, PORT_LOCA, PORT_BCOL, PORT_FCOL, 1, 11, "80, 443, 8080, 53, 56, 21, 22, 22565");

		PORT.ScrollBars = RichTextBoxScrollBars.ForcedVertical;

		var RECT_SIZE = new Size(PORTCONTAINER_SIZE.Width - 2, PORTCONTAINER_SIZE.Height - 2);
		var RECT_LOCA = new Point(1, 1);
		var RECT_COLA = GUI.BAR.BackColor;

		TOOL.PaintRectangle(PORTCONTAINER, 2, RECT_SIZE, RECT_LOCA, RECT_COLA);

		var LOGCONTAINER_SIZE = new Size(PORTCONTAINER_SIZE.Width, TOP.Height - PORTCONTAINER.Top - PORTCONTAINER.Height - 20);
		var LOGCONTAINER_LOCA = new Point(PORTCONTAINER_LOCA.X, PORTCONTAINER.Top + PORTCONTAINER.Height + 10);
		var LOGCONTAINER_BCOL = PORT_BCOL;

		CONTROL.Image(TOP, LOGSCONTAINER, LOGCONTAINER_SIZE, LOGCONTAINER_LOCA, null, LOGCONTAINER_BCOL);
		TOOL.Round(LOGSCONTAINER, 6);

		var LOG_SIZE = new Size(LOGCONTAINER_SIZE.Width - 10, LOGCONTAINER_SIZE.Height - 10);
		var LOG_LOCA = new Point(5, 5);
		var LOG_BCOL = PORT_BCOL;
		var LOG_FCOL = PORT_FCOL;

		CONTROL.RichTextBox(LOGSCONTAINER, LOGS, LOG_SIZE, LOG_LOCA, LOG_BCOL, LOG_FCOL, 1, 9, "(!) Waiting for you man ....\r\n");
	
		LOGS.ScrollBars = RichTextBoxScrollBars.ForcedVertical;
		LOGS.ReadOnly = true;

		RECT_SIZE = new Size(LOGCONTAINER_SIZE.Width - 2, LOGCONTAINER_SIZE.Height - 2);

		TOOL.PaintRectangle(LOGSCONTAINER, 2, RECT_SIZE, RECT_LOCA, RECT_COLA);

		var CONTAINER_SIZE = new Size(160, 22);
		var CONTAINER_LOCA = new Point((LOG_SIZE.Width - CONTAINER_SIZE.Width) / 2 - 10, LOG_SIZE.Height - CONTAINER_SIZE.Height - 5);
		var CONTAINER_COLA = Color.Transparent;

		CONTROL.Image(LOGS, CONTAINER, CONTAINER_SIZE, CONTAINER_LOCA, null, CONTAINER_COLA);
		
		TOOL.Round(YES, 6);
		TOOL.Round(NUU, 6);

		var OPTION_SIZE = new Size((CONTAINER_SIZE.Width - 10) / 2, CONTAINER_SIZE.Height);
		var OPTION_LOCA = new Point(0, 0);
		var OPTION_BCOL = Color.MidnightBlue;
		var OPTION_FCOL = Color.White;
		
		CONTROL.Button(CONTAINER, YES, OPTION_SIZE, OPTION_LOCA, OPTION_BCOL, OPTION_FCOL, 1, 10, "Yes", Color.Empty);
		
		OPTION_LOCA.X += OPTION_SIZE.Width + 10;

		CONTROL.Button(CONTAINER, NUU, OPTION_SIZE, OPTION_LOCA, OPTION_BCOL, OPTION_FCOL, 1, 10, "Nuu", Color.Empty);
	    }
	}

	public PortScanner()
	{
	    InitializeComponent();

	    try
	    {
		GUI.InitializeGUI(this);
		OPS.InitializeContainer(this);

		// Initialize Event Handlers
		// -------------------------
		// Recognize 1,2 1-2 and 80.
	    }

	    catch (Exception e)
	    {
		SendError($"PortScanner::{e.Message}");
	    }
	}

	public void InitializeComponent()
	{
	    SuspendLayout();

	    MaximumSize = new Size(300, 300);
	    MinimumSize = new Size(300, 300);

	    StartPosition = FormStartPosition.CenterParent;

	    MaximizeBox = false;
	    MinimizeBox = false;

	    Name = "PortScanner";
	    Text = "Port Scanner";

	    ResumeLayout(false);
	}
    }
}