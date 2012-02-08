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

public class ContactItemsIterator
{
    private static final Logger LOG = Logger.getInstance(ContactItemsIterator.class);

    private _NameSpace _mapiNS;

    public static void main(String[] args)
    {
        ContactItemsIterator p = new ContactItemsIterator();
        int result = p.execute();
        System.exit(result);
    }

    private int execute()
    {
        init();
        try
        {
            login();

            MAPIFolder contactsFolder = getContactsFolder();
            _Items items = contactsFolder.getItems();
            int count = new Long(items.getCount().getValue()).intValue();
            System.out.println("Count: " + items.getCount().getValue());

            _ContactItem contactItem = ContactItem.queryInterface(items.getFirst());
            print(contactItem);
            for (int i = 0; i < count - 1; i++) {
                final IDispatch next = items.getNext();
                try {
                    contactItem = ContactItem.queryInterface(next);
                    print(contactItem);
                }
                catch (ComException e) {
                    // probably this is not a contact item but Distribution List item
                    _DistListItem distListItem = DistListItem.queryInterface(next);
                    print(distListItem);
                }
            }
            return 0;
        }
        catch (ComException e) {
            LOG.error("Unable to access Outlook profile.", e);
            return -1;
        }
        finally
        {
            shutdown();
        }
    }

    void print(_ContactItem contactItem) {
        System.out.println("Contact Item:");
        System.out.println("\tFirstName = " + contactItem.getFirstName().getValue());
        System.out.println("\tFirstName = " + contactItem.getLastName().getValue());
        System.out.println("\tJobTitle = " + contactItem.getJobTitle().getValue());
    }

    void print(_DistListItem distListItem) {
        System.out.println("DistList Item:");
        System.out.println("\tName = " + distListItem.getDLName().getValue());
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

    private MAPIFolder getContactsFolder() throws ComException
    {
        MAPIFolder result = _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderContacts));
        return result;
    }
}