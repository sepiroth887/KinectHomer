/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import outlook.outlook.*;
import outlook.outlook.impl._ContactItemImpl;
import outlook.outlook.impl._DistListItemImpl;

/**
 * This sample demonstrates working with Outlook contacts.
 *
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Outlook 11.0 Object Library
 * ProgID:      Outlook.Application
 * GUID:        {00062FFF-0000-0000-C000-000000000046}
 * In the package: outlook
 *
 * You can generate stubs using the Code Generator application.
 *
 * @author Alexei Orischenko
 */
public class OutlookContactsSample
{
    private _NameSpace _mapiNS;
    private _Application _application;

    public static void main(String[] args)
    {
        OleFunctions.oleInitialize();
        try
        {
            final OutlookContactsSample outlookContactsSample = new OutlookContactsSample();
            outlookContactsSample.login();
            outlookContactsSample.printContacts();
            outlookContactsSample.logout();
        }
        finally
        {
            OleFunctions.oleUninitialize();
        }
    }

    public void printContacts()
    {
        MAPIFolder folder = getContactsFolder();

        _Items items = folder.getItems();

        int count = (int)items.getCount().getValue();
        for (int i = 1; i <= count; i++)
        {
            printItem(items, i);
        }
    }

    private void printItem(_Items items, int i)
    {
        IDispatch item = items.item(new Variant(i));

        try
        {
            _ContactItem contact = new _ContactItemImpl(item);

            String name = contact.getFullName().getValue();
            String company = contact.getCompanyName().getValue() != null ? contact.getCompanyName().getValue() : "";
            System.out.println("ContactList Item:");
            System.out.println("\tName = " + name + ", company: " + company);
        }
        catch (ComException e)
        {
            // probably this is not a contact item but Distribution List item
            _DistListItem distListItem = new _DistListItemImpl(item);

                System.out.println("DistList Item:");
                System.out.println("\tName = " + distListItem.getDLName().getValue());
        }
    }

    public void logout()
    {
        _mapiNS.logoff();
    }

    public void login() throws ComException
    {
        _application = Application.create(ClsCtx.LOCAL_SERVER);

        _mapiNS = _application.getNamespace(new BStr("MAPI"));

        _mapiNS.logon(new Variant("Outlook"),
                new Variant(""),
                new Variant(false),
                new Variant(false));
    }

    private MAPIFolder getContactsFolder() throws ComException
    {
        return _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderContacts));
    }
}