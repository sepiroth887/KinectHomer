/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.util.Logger;
import outlook.outlook.*;

public class MailItemsIterator
{
    private static final Logger LOG = Logger.getInstance(MailItemsIterator.class);

    private _NameSpace _mapiNS;

    public static void main(String[] args)
    {
        MailItemsIterator p = new MailItemsIterator();
        int result = p.execute();
        System.exit(result);
    }

    private int execute() {
        init();
        try {
            login();
            MAPIFolder folder = _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderInbox));
            _Items items = folder.getItems();
            int count = new Long(items.getCount().getValue()).intValue();
            System.out.println("Mail Items count: " + items.getCount().getValue());

            _MailItem mailItem = MailItem.queryInterface(items.getFirst());
            print(mailItem);
            for (int i = 0; i < count - 1; i++) {
                final IDispatch next = items.getNext();
                try {
                    mailItem = MailItem.queryInterface(next);
                    print(mailItem);
                }
                catch (ComException e) {
                    // Probably this is a report item?
                    _ReportItem reportItem = ReportItem.queryInterface(next);
                    print(reportItem);
                }
            }
            return 0;
        }
        catch (ComException e) {
            LOG.error("Unable to access Outlook profile.", e);
            return -1;
        }
        finally {
            shutdown();
        }
    }

    void print(_MailItem item) {
        System.out.println("Mail Item:");
        System.out.println("\tSubject: " + item.getSubject().getValue());
        System.out.println("\tSender : " + item.getSenderName().getValue());
    }

    void print(_ReportItem item) {
        System.out.println("Report Item:");
        System.out.println("\tSubject: " + item.getSubject().getValue());
    }

    private void login() throws ComException
    {
        _Application application = Application.create(ClsCtx.LOCAL_SERVER);
        _mapiNS = application.getNamespace(new BStr("MAPI"));

        _mapiNS.logon(new Variant("Outlook"),
                new Variant(""),
                new Variant(false),
                new Variant(false));
    }

    private static void init()
    {
        OleFunctions.oleInitialize();
    }

    private static void shutdown()
    {
        OleFunctions.oleUninitialize();
    }
}