/*****************************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/View.java,v $
 * $Revision: 1.48 $
 * $Date: 2009/12/16 00:11:59 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 ****************************************************************************/
package de.willuhn.jameica.gui;



import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.Font;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.system.Customizing;

/**
 * Bildet das Content-Frame ab.
 * @author willuhn
 */
public class View implements Part
{
	private SashForm sash;

	private Composite view;
	private ScrolledComposite scroll;
	private Composite content;
	private Composite snapin;
	private boolean snappedIn    = false;


	private Composite parent;
	private CLabel messages;
  
  private Canvas logoBg;
  private Canvas panelBg;
	

  private String title;
  private String logotext;

  /**
   * @see de.willuhn.jameica.gui.Part#paint(org.eclipse.swt.widgets.Composite)
   */
  public void paint(Composite parent) throws RemoteException
  {
    this.parent = parent;
    init();
    cleanContent();
  }
  
  /**
   * Initialisiert das Layout der View.
   */
  private void init()
	{
		sash = new SashForm(parent,SWT.VERTICAL);
    sash.setLayoutData(new GridData(GridData.FILL_BOTH));
		sash.setLayout(SWTUtil.createGrid(1,true));
		
		view = new Composite(sash, SWT.BORDER);
    view.setBackground(Color.BACKGROUND.getSWTColor());
    view.setLayoutData(new GridData(GridData.FILL_BOTH));
		view.setLayout(SWTUtil.createGrid(1,true));


		snapin = new Composite(sash, SWT.BORDER);
		snapin.setLayoutData(new GridData(GridData.FILL_BOTH));
		snapin.setLayout(SWTUtil.createGrid(1,true));
		sash.setMaximizedControl(view);

		if (!Customizing.SETTINGS.getBoolean("application.view.hidelogo",false))
		{
	    ////////////////////////////////////////////////////////////////////////////
	    //
	    final Image logo = SWTUtil.getImage("panel.bmp");
	    final Rectangle imageSize = logo.getBounds();
	    logoBg = SWTUtil.getCanvas(view,logo, SWT.TOP | SWT.RIGHT);
	    logoBg.setBackground(new org.eclipse.swt.graphics.Color(GUI.getDisplay(),255,255,255));
	    logoBg.setLayout(SWTUtil.createGrid(1,false));

	    logoBg.addListener(SWT.Paint, new Listener()
	    {
	      public void handleEvent(Event event)
	      {
	        GC gc = event.gc;
	        Rectangle size = logoBg.getBounds();
	        gc.setBackground(new org.eclipse.swt.graphics.Color(GUI.getDisplay(),255,255,255));
	        gc.fillRectangle(size);
	        gc.drawImage(logo,size.width - imageSize.width,0);
	        gc.setFont(Font.SMALL.getSWTFont());
	        gc.setForeground(Color.COMMENT.getSWTColor());
	        gc.drawText(logotext == null ? "" : logotext,8,14,true);
	      }
	    });
	    ////////////////////////////////////////////////////////////////////////////

	    Label sep = new Label(view,SWT.SEPARATOR | SWT.HORIZONTAL);
	    sep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

    if (!Customizing.SETTINGS.getBoolean("application.view.hidepanel",false))
    {
      ////////////////////////////////////////////////////////////////////////////
      //
      panelBg = SWTUtil.getCanvas(view,SWTUtil.getImage("panel-reverse.gif"), SWT.TOP | SWT.RIGHT);
      panelBg.setLayout(SWTUtil.createGrid(1,false));
      panelBg.setBackground(Color.BACKGROUND.getSWTColor());

      panelBg.addListener(SWT.Paint,new Listener()
      {
        public void handleEvent(Event event)
        {
          GC gc = event.gc;
          gc.setFont(Font.H2.getSWTFont());
          gc.drawText(title == null ? "" : title,8,1,true);
        }
      });
      ////////////////////////////////////////////////////////////////////////////

      Label sep2 = new Label(view,SWT.SEPARATOR | SWT.HORIZONTAL);
      sep2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    if (!Customizing.SETTINGS.getBoolean("application.view.hidemessages",false))
    {
      ////////////////////////////////////////////////////////////////////////////
      //
  		messages = new CLabel(view,SWT.NONE);
  		messages.setFont(Font.H2.getSWTFont());
      messages.setBackground(new org.eclipse.swt.graphics.Color(GUI.getDisplay(),255,255,255));
  		messages.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

  		Label sep3 = new Label(view,SWT.SEPARATOR | SWT.HORIZONTAL);
      sep3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      ////////////////////////////////////////////////////////////////////////////
    }
	}
	
  /**
   * Leert die Anzeige.
   * Wird beim Wechsel von einem Dialog auf den naechsten aufgerufen.
   */
  protected void cleanContent()
	{
		if (content != null && !content.isDisposed())
			content.dispose();
		
		if (scroll != null && !scroll.isDisposed())
		  scroll.dispose();
		

		if (Customizing.SETTINGS.getBoolean("application.scrollview",false))
		{
      scroll = new ScrolledComposite(view,SWT.V_SCROLL | SWT.H_SCROLL);
      scroll.setBackground(Color.BACKGROUND.getSWTColor());
      scroll.setLayoutData(new GridData(GridData.FILL_BOTH));
      scroll.setLayout(SWTUtil.createGrid(1,true));
      scroll.setExpandHorizontal(true);
      scroll.setExpandVertical(true);
      scroll.setMinHeight(Customizing.SETTINGS.getInt("application.scrollview.minheight",580));

      content = new Composite(scroll, SWT.NONE);
      scroll.setContent(content);
		}
		else
		{
	    content = new Composite(view, SWT.NONE);
		}
		
		content.setBackground(Color.BACKGROUND.getSWTColor());
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout l = new GridLayout();
		l.marginHeight = 6;
		l.marginWidth = 6;
		content.setLayout(l);

		if (messages != null)
		{
	    messages.setText("");
	    messages.layout();
		}
	}

	/**
   * Das Snapin-Composite wird angezeigt.
   */
  public void snapIn()
	{
		GUI.startSync(new Runnable()
    {
      public void run()
      {
				sash.setMaximizedControl(null);
        int[] weights = new int[] {GUI.SETTINGS.getInt("snapin.height.0",3),GUI.SETTINGS.getInt("snapin.height.1",1)};
        if (weights[0] <= 0) weights[0] = 3;
        if (weights[1] <= 0) weights[1] = 1;
        sash.setWeights(weights);
				snappedIn = true;
      }
    });
	}

	/**
   * Das Snapin-Composite wird ausgeblendet.
   */
  public void snapOut()
	{
		GUI.startSync(new Runnable()
    {
      public void run()
      {
        try
        {
          // BUGZILLA 432 - Speichern der letzten Hoehe des Snapins
          int[] weights = sash.getWeights();
          GUI.SETTINGS.setAttribute("snapin.height.0",weights[0]);
          GUI.SETTINGS.setAttribute("snapin.height.1",weights[1]);
          SWTUtil.disposeChildren(snapin);
          sash.setMaximizedControl(view);
        }
        finally
        {
          snappedIn = false;
        }
      }
    });
	}

	/**
	 * Prueft, ob das Snapin gerade angezeigt wird.
   * @return true, wenn es angezeigt wird.
   */
  public boolean snappedIn()
	{
		return snappedIn;
	}

	/**
	 * Liefert das SnapIn-Composite.
	 * Die Funktion liefert immer ein leeres Snapin. Wenn sich also vorher
	 * was drin befunden hat, wird es vorm erneuten Herausgeben geleert.
	 * Hinweis: Das Composite enthaelt ein GridLayout.
   * @return Snapin-Composite.
   */
  public Composite getSnapin()
	{
		return snapin;
	}

	/**
	 * Aktualisiert den Titel der View.
   * @param text anzuzeigender Titel.
   */
  public void setTitle(String text)
	{
    this.title = text == null ? "" : text;
    if (this.panelBg != null && !this.panelBg.isDisposed())
    {
      GUI.getDisplay().syncExec(new Runnable() {
        public void run()
        {
          panelBg.redraw();
        }
      });
    }
	}

  /**
   * Aktualisiert den Text neben dem Logo.
   * Normalerweise steht da nichts. Man kann
   * aber was hinschreiben.
   * @param text der text links neben dem Logo.
   */
  public void setLogoText(String text)
  {
    this.logotext = text == null ? "" : text;
    if (this.logoBg != null && !this.logoBg.isDisposed())
    {
      GUI.getDisplay().asyncExec(new Runnable() {
        public void run()
        {
          logoBg.redraw();
        }
      });
    }
  }

  /**
	 * Schreibt einen Fehlertext oben in die View.
   * @param text anzuzeigender Text. 
   */
  public void setErrorText(final String text)
	{
    setStatusText(text,Color.ERROR);
	}

  /**
   * Private Hilfs-Funktion, die den Text anzeigt.
   * @param text anzuzeigender Text.
   * @param color Farbe.
   */
  private void setStatusText(final String text, final Color color)
  {
    if (this.messages != null && !this.messages.isDisposed())
    {
      final long currentClick = System.currentTimeMillis();

      GUI.getDisplay().asyncExec(new Runnable() {
        public void run() {
          messages.setText(text == null ? "" : " " + text);
          messages.setForeground(color.getSWTColor());
          lastClick = currentClick;
        }
      });
      GUI.getDisplay().timerExec(10000,new Runnable()
      {
        public void run()
        {
          if (currentClick == lastClick && !messages.isDisposed()) // nur entfernen, wenn wir der letzte Klick waren
            messages.setText("");
        }
      });
    }
  }
	private long lastClick;

	/**
	 * Schreibt einen Erfolgstext oben in die View.
	 * @param text anzuzeigender Text. 
	 */
	public void setSuccessText(final String text)
	{
    setStatusText(text,Color.SUCCESS);
	}

  /**
   * Aktualisiert die Anzeige.
   */
  protected void refreshContent()
	{
    view.layout();
	}

  /**
   * Liefert das Composite, in das die anzuzeigenden Dialoge bitte ihre Controls reinmalen sollen.
   * @return Composite, in das der Dialog seine Elemente reinmalen soll.
   */
  protected Composite getContent()
	{
		return content;
	}
}



/***************************************************************************
 * $Log: View.java,v $
 * Revision 1.48  2009/12/16 00:11:59  willuhn
 * @N Scroll-Support fuer Views - nochmal ueberarbeitet und jetzt via Customizing konfigurierbar
 *
 * Revision 1.47  2009/10/12 08:55:36  willuhn
 * *** empty log message ***
 *
 * Revision 1.46  2009/06/04 10:36:01  willuhn
 * @N Customizing-Parameter zum Ausblenden von Logo-Bar, Message-Bar und Title-Panel
 *
 * Revision 1.45  2009/03/20 16:38:09  willuhn
 * @N BUGZILLA 576
 ***************************************************************************/