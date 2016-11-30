package com.horstmann.violet.framework.theme;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.pagosoft.plaf.PgsLookAndFeel;
import com.pagosoft.plaf.PgsTheme;
import com.pagosoft.plaf.PlafOptions;

public class DarkOrangeTheme extends AbstractTheme {

	@Override
	public ThemeInfo getThemeInfo() {
		return new ThemeInfo("Dark Orange", DarkOrangeTheme.class, PgsLookAndFeel.class);
	}
	
    @Override
    protected void configure()
    {
    	UIDefaults defaults = UIManager.getDefaults();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("MenuItem.background", new Color(255, 255, 255));
        m.put("MenuBar.background", new Color(255, 255, 255));
        defaults.putAll(m);
        
        OrangeTheme orangeTheme = new OrangeTheme()
        {
            public ColorUIResource getMenuBackground()
            {
                return new ColorUIResource(new Color(255, 255, 255));
            }

            public ColorUIResource getSecondary3()
            {
                return new ColorUIResource(new Color(244, 244, 244));
            }
        };

        PgsLookAndFeel.setCurrentTheme(orangeTheme);
    }
	
	
    




    public Color getWhiteColor()
    {
        return Color.WHITE;
    }

    public Color getBlackColor()
    {
        return Color.BLACK;
    }

    public Color getGridColor()
    {
        return new Color(250, 250, 250);
    }

    public Color getBackgroundColor()
    {
        return new Color(244 ,244,244);
    }

    public Font getMenubarFont()
    {
        return MetalLookAndFeel.getMenuTextFont();
    }

    public Color getMenubarBackgroundColor()
    {
        return new Color(242, 241, 240);
    }

    public Color getMenubarForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getRolloverButtonDefaultColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getRolloverButtonRolloverBorderColor()
    {
        return getMenubarForegroundColor();
    }

    public Color getRolloverButtonRolloverColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getSidebarBackgroundEndColor()
    {
        return new Color(244, 244, 244);
    }

    public Color getSidebarBackgroundStartColor()
    {
        return new Color(244, 244, 244);
    }

    public Color getSidebarBorderColor()
    {
        return getBackgroundColor();
    }

    public Color getSidebarElementBackgroundColor()
    {
        return getBackgroundColor();
    }

    public Color getSidebarElementTitleBackgroundEndColor()
    {
        return new Color(54, 69, 89);
    }

    public Color getSidebarElementTitleBackgroundStartColor()
    {
        return new Color(54, 69, 89);
    }

    public Color getSidebarElementForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getSidebarElementTitleOverColor()
    {
        return Color.WHITE;
    }

    public Color getStatusbarBackgroundColor()
    {
        return new Color(100, 100, 100);
    }

    public Color getStatusbarBorderColor()
    {
        return getMenubarBackgroundColor();
    }

    public Font getToggleButtonFont()
    {
        return MetalLookAndFeel.getMenuTextFont().deriveFont(Font.PLAIN);
    }

    public Color getToggleButtonSelectedBorderColor()
    {
        return new Color(247, 154, 24);
    }

    public Color getToggleButtonSelectedColor()
    {
        return new Color(255, 203, 107);
    }

    public Color getToggleButtonUnselectedColor()
    {
        return getSidebarElementBackgroundColor();
    }

    public Font getWelcomeBigFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 28.0);
    }

    public Font getWelcomeSmallFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 12.0).deriveFont(Font.PLAIN);
    }

    public Color getWelcomeBackgroundEndColor()
    {
        return new Color(120, 120, 120);
    }

    public Color getWelcomeBackgroundStartColor()
    {
        return new Color(150, 150, 150);
    }

    public Color getWelcomeBigForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getWelcomeBigRolloverForegroundColor()
    {
        return new Color(255, 203, 151);
    }

    
    private class OrangeTheme extends PgsTheme
    {
        public OrangeTheme()
        {
            super("Orange");
//
            setSecondary1(new ColorUIResource(0xEE8104));
            setSecondary2(new ColorUIResource(0xFF3300));
            setSecondary3(new ColorUIResource(0xD49923));

            setPrimary1(new ColorUIResource(0xB0863C));
            setPrimary2(new ColorUIResource(0xFFDAB3));
            setPrimary3(new ColorUIResource(0xFDF0DE));

            setBlack(new ColorUIResource(Color.BLACK));
            setWhite(new ColorUIResource(Color.WHITE));

            PlafOptions.setOfficeScrollBarEnabled(true);
            PlafOptions.setVistaStyle(true);
            PlafOptions.useBoldFonts(false);

            setDefaults(new Object[]
            {
                    "MenuBar.isFlat",
                    Boolean.FALSE,
                    "MenuBar.gradientStart",
                    new ColorUIResource(49, 54, 71),
                    "MenuBar.gradientMiddle",
                    new ColorUIResource(49, 54, 71),
                    "MenuBar.gradientEnd",
                    new ColorUIResource(49, 54, 71),

                    "MenuBarMenu.isFlat",
                    Boolean.FALSE,
                    "MenuBarMenu.foreground",
                    getWhite(),
                    "MenuBarMenu.rolloverBackground.gradientStart",
                    new ColorUIResource(216, 90, 36),
                    "MenuBarMenu.rolloverBackground.gradientMiddle",
                    new ColorUIResource(216, 90, 36),
                    "MenuBarMenu.rolloverBackground.gradientEnd",
                    new ColorUIResource(216, 90, 36),
                    
                    "MenuBarMenu.selectedBackground.gradientStart",
                    new ColorUIResource(243, 131, 83),
                    "MenuBarMenu.selectedBackground.gradientMiddle",
                    new ColorUIResource(243, 131, 83),
                    "MenuBarMenu.selectedBackground.gradientEnd",
                    new ColorUIResource(243, 131, 83),
                    "MenuBarMenu.rolloverBorderColor",
                    getPrimary3(),
                    "MenuBarMenu.selectedBorderColor",
                    getPrimary3(),

                    "Menu.gradientStart",
                    getPrimary3(),
                    "Menu.gradientEnd",
                    getPrimary3(),
                    "Menu.gradientMiddle",
                    getPrimary3(),
                    "Menu.isFlat",
                    Boolean.FALSE,

                    "MenuItem.gradientStart",
                    getPrimary3(),
                    "MenuItem.gradientEnd",
                    getPrimary3(),
                    "MenuItem.gradientMiddle",
                    getPrimary3(),
                    "MenuItem.isFlat",
                    Boolean.FALSE,

                    "CheckBoxMenuItem.gradientStart",
                    getPrimary3(),
                    "CheckBoxMenuItem.gradientEnd",
                    getPrimary3(),
                    "CheckBoxMenuItem.gradientMiddle",
                    getPrimary3(),
                    "CheckBoxMenuItem.isFlat",
                    Boolean.FALSE,

                    "RadioButtonMenuItem.gradientStart",
                    getPrimary3(),
                    "RadioButtonMenuItem.gradientEnd",
                    getPrimary3(),
                    "RadioButtonMenuItem.gradientMiddle",
                    getPrimary3(),
                    "RadioButtonMenuItem.isFlat",
                    Boolean.FALSE,

                    "Button.rolloverGradientStart",
                    getPrimary3(),
                    "Button.rolloverGradientEnd",
                    getPrimary3(),
                    "Button.selectedGradientStart",
                    getPrimary3(),
                    "Button.selectedGradientEnd",
                    getPrimary3(),
                    "Button.rolloverVistaStyle",
                    Boolean.TRUE,
                    "glow",
                    getPrimary1(),

                    "ToggleButton.rolloverGradientStart",
                    getPrimary3(),
                    "ToggleButton.rolloverGradientEnd",
                    getPrimary3(),
                    "ToggleButton.selectedGradientStart",
                    getPrimary3(),
                    "ToggleButton.selectedGradientEnd",
                    getPrimary3(),

                    "TabbedPane.selected",
                    new ColorUIResource(253, 236, 178),
                    "TabbedPane.background",
                    new ColorUIResource(Color.WHITE),
                    "TabbedPane.selectedForeground",
                    new ColorUIResource(Color.BLACK),

            });
        }
    }
}
