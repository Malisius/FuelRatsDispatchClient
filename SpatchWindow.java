package com.cmdrsforhire;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;

public class SpatchWindow {

	public List ratChatList;
	public List fuelRatsList;
	public List serverList;
	public List caseChatList;
	
	protected Shell shell;
	private Text text;


	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(605, 454);
		shell.setText("SWT Application");
		GridLayout gl_shell = new GridLayout(4, false);
		gl_shell.verticalSpacing = 3;
		shell.setLayout(gl_shell);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmConnection = new MenuItem(menu_1, SWT.CASCADE);
		mntmConnection.setText("Connection");
		
		Menu menu_2 = new Menu(mntmConnection);
		mntmConnection.setMenu(menu_2);
		
		MenuItem mntmConnect = new MenuItem(menu_2, SWT.NONE);
		mntmConnect.setText("Connect");
		
		MenuItem mntmDisconnect = new MenuItem(menu_2, SWT.NONE);
		mntmDisconnect.setText("Disconnect");
		
		MenuItem mntmSettings = new MenuItem(menu_2, SWT.NONE);
		mntmSettings.setText("Settings");
		
		MenuItem mntmQuit = new MenuItem(menu_1, SWT.NONE);
		mntmQuit.setText("Quit");
		mntmQuit.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
		});
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Label lblCases = new Label(composite, SWT.NONE);
		lblCases.setText("Cases:");
		
		ListViewer listViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List caseList = listViewer.getList();
		caseList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(1, false));
		
		Label lblCmdrs = new Label(composite_1, SWT.NONE);
		lblCmdrs.setText("CMDRs:");
		
		ListViewer listViewer_1 = new ListViewer(composite_1, SWT.BORDER | SWT.V_SCROLL);
		List ratsList = listViewer_1.getList();
		ratsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(1, false));
		
		Label lblCommands = new Label(composite_2, SWT.NONE);
		lblCommands.setText("Commands:");
		
		TreeViewer treeViewer = new TreeViewer(composite_2, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(shell, SWT.NONE);
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 2));
		
		TabFolder tabFolder = new TabFolder(composite_3, SWT.NONE);
		
		TabItem tbtmServer = new TabItem(tabFolder, SWT.NONE);
		tbtmServer.setText("Server");
		
		serverList = new List(tabFolder, SWT.BORDER);
		tbtmServer.setControl(serverList);
		
		TabItem tabmFuelRats = new TabItem(tabFolder, SWT.NONE);
		tabmFuelRats.setText("#fuelrats");
		
		fuelRatsList = new List(tabFolder, SWT.BORDER);
		tabmFuelRats.setControl(fuelRatsList);
		
		TabItem tbtmRatChat = new TabItem(tabFolder, SWT.NONE);
		tbtmRatChat.setText("#ratchat");
		
		ratChatList = new List(tabFolder, SWT.BORDER);
		tbtmRatChat.setControl(ratChatList);
		
		TabItem tbtmCaseChat = new TabItem(tabFolder, SWT.NONE);
		tbtmCaseChat.setText("Case Chat");
		
		caseChatList = new List(tabFolder, SWT.BORDER);
		tbtmCaseChat.setControl(caseChatList);
		
		text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnSend = new Button(shell, SWT.NONE);
		btnSend.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSend.setText("Send");

	}
}
