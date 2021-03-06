
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
    class Entity
    {
	public readonly List<PictureBox> Characters = new List<PictureBox>();

	public class EntityType
	{
	    public enum Types
	    {
		PONY, PUG
	    };

	    public class Pony
	    {
		public static readonly List<List<string>> descr = new List<List<string>>();
		public static readonly List<PictureBox> ponies = new List<PictureBox>();
		public static readonly List<string> names = new List<string>();
		public static readonly List<int> prices = new List<int>();
	    };

	    public class Pug
	    {
		public static readonly List<List<string>> descr = new List<List<string>>();
		public static readonly List<PictureBox> pugs = new List<PictureBox>();
		public static readonly List<string> names = new List<string>();
		public static readonly List<int> prices = new List<int>();
	    };
	};

	//private readonly Form Owner = Lunaroc.GetOwner();

	void LoadConfiguration(EntityType.Types TYPE)
	{
	    string[] data = File.ReadAllLines($@"data\config\character\{TYPE.ToString().ToLower()}.yml");
	    List<string> desc = new List<string>();

	    // ADD DESCRIPTION TO CONFIG
	    for (int l = 0; l < data.Length; l += 1)
	    {
		data[l] = Strings.formatConfigLine(Strings.removeEmpty(data[l]));

		if (data[l].Equals("description"))
		{
		    for (int k = l; k < data.Length; k += 1)
		    {
			string desl = Strings.removeEmpty(data[k]);

			if (desl.StartsWith("-"))
			{
			    desc.Add(desl.Replace("- ", string.Empty));		    
			};
		    };
		}

		else if (Integers.IsNumeric(data[l]) && !data[l].Contains("#"))
		{
		    string name = Strings.formatConfigLine(Strings.removeEmpty(data[l + 1]));
		    string buy = Strings.formatConfigLine(Strings.removeEmpty(data[l + 2]));

		    PictureBox character = new PictureBox
		    {
			Image = Image.FromFile($@"data\characters\{TYPE.ToString().ToLower()}\{EntityType.Pony.names.Count + 1}.gif"),
			BackColor = Color.FromArgb(0, 0, 0, 255)
		    };

		    character.Size = character.Image.Size;

		    try
		    {
			switch (TYPE.ToString())
			{
			    case "PONY":
			    {
				EntityType.Pony.names.Add(name);
				EntityType.Pony.prices.Add(Int32.Parse(buy));
				EntityType.Pony.ponies.Add(character);
				EntityType.Pony.descr.Add(desc);

				break;
			    };

			    case "PUG":
			    {
				EntityType.Pug.names.Add(name);
				EntityType.Pug.prices.Add(Int32.Parse(buy));
				EntityType.Pug.pugs.Add(character);
				EntityType.Pug.descr.Add(desc);

				break;
			    };
			};
		    }

		    catch
		    {
			// ERROR HANDLING? 
		    };
		    
		    l += 2;
		};
	    };
	}

	public void LoadCharacters()
	{
	    try
	    {
		foreach (EntityType.Types TYPE in Enum.GetValues(typeof(EntityType.Types)))
		{
		    LoadConfiguration(TYPE);
		};
	    }

	    catch { };
	}
    };
};