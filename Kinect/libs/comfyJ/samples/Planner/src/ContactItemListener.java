/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import outlook.outlook.server.ItemEventsServer;
import outlook.outlook.*;

public class ContactItemListener
{
    private _NameSpace _mapiNS;

    public static class ContactItemEventsListener extends ItemEventsServer
    {
        public ContactItemEventsListener(CoClassMetaInfo classImpl)
        {
            super(classImpl);
        }

        public void read()
        {
            System.out.println("ContactItemEventsListener, on read");
        }

        public void close(VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("ContactItemEventsListener, on close");
        }

        public void reply(IDispatch /*[in]*/ Response, VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("ContactItemEventsListener, on reply");
        }

        public void open(VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("ContactItemEventsListener, on open");
        }

        public static void attachTo(_ContactItem contactItem)
        {
            IClassFactoryServer server = new IClassFactoryServer(ContactItemEventsListener.class);
            server.registerInterface(IDispatch.class, new IDispatchVTBL(server));
            server.setDefaultInterface(IDispatch.class);

            IClassFactory factory = server.createIClassFactory();
            IDispatchImpl handler = new IDispatchImpl();
            factory.createInstance(null, handler.getIID(), handler);

            IConnectionPointContainer cpc = new IConnectionPointContainerImpl(contactItem);
            IConnectionPoint cp = cpc.findConnectionPoint(new IID(ItemEvents.INTERFACE_IDENTIFIER));
            cp.advise(handler);
        }
    }

    public void execute() throws Exception
    {
        OleMessageLoop.invokeAndWait(new Runnable()
        {
            public void run()
            {
                login();

                MAPIFolder contactsFolder = _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderContacts));

                _Items items = contactsFolder.getItems();
                int count = new Long(items.getCount().getValue()).intValue();
                System.out.println("Count: " + items.getCount().getValue());

                if (count > 0)
                {
                    _ContactItem contactItem = ContactItem.queryInterface(items.getFirst());

                    System.out.println("Contact Item:");
                    System.out.println("\tFirstName = " + contactItem.getFirstName().getValue());
                    System.out.println("\tLastName = " + contactItem.getLastName().getValue());

                    ContactItemEventsListener.attachTo(contactItem);
                }
            }
        });
    }

    private void login() throws ComException
    {
        _Application application = Application.create(ClsCtx.LOCAL_SERVER);
        _mapiNS = application.getNamespace(new BStr("MAPI"));
        _mapiNS.logon(new Variant("Outlook"), new Variant(""), new Variant(false), new Variant(false));
    }

    public static void main(String[] args) throws Exception
    {
        ContactItemListener listener = new ContactItemListener();
        listener.execute();
        System.out.println("Press 'Enter' to terminate example");
        System.in.read();
    }
}