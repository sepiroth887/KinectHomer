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
import outlook.outlook.server.ItemEvents_10Server;
import outlook.outlook.*;

/**
 * This sample demonstrates how to setup Mail Item listener.
 *
 * @author Serge Piletsky
 */
public class MailItemListener
{
    private _NameSpace _mapiNS;

    public static class MailItemEventsListener extends ItemEvents_10Server
    {
        public MailItemEventsListener(CoClassMetaInfo classImpl)
        {
            super(classImpl);
        }

        public void read()
        {
            System.out.println("MailItemEventsListener, on read");
        }

        public void close(VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("MailItemEventsListener, on close");
        }

        public void reply(IDispatch /*[in]*/ Response, VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("MailItemEventsListener, on reply");
        }

        public void open(VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("MailItemEventsListener, on open");
        }

        public void beforeDelete(IDispatch /*[in]*/ Item, VariantBool /*[in,out]*/ Cancel)
        {
            System.out.println("beforeDelete");
            // Cancel deleting by setting Cancel boolean parameter to true
            Cancel.setBooleanValue(true);
        }

        public static void attachTo(_MailItem item)
        {
            IClassFactoryServer server = new IClassFactoryServer(MailItemEventsListener.class);
            server.registerInterface(IDispatch.class, new IDispatchVTBL(server));
            server.setDefaultInterface(IDispatch.class);

            IClassFactory factory = server.createIClassFactory();
            IDispatchImpl handler = new IDispatchImpl();
            factory.createInstance(null, handler.getIID(), handler);

            IConnectionPointContainer cpc = new IConnectionPointContainerImpl(item);
            IConnectionPoint cp = cpc.findConnectionPoint(new IID(ItemEvents_10.INTERFACE_IDENTIFIER));
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

                MAPIFolder folder = _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderInbox));

                _Items items = folder.getItems();
                int count = new Long(items.getCount().getValue()).intValue();
                System.out.println("Mail Items Count: " + items.getCount().getValue());
                if (count > 0)
                {
                    _MailItem item = MailItem.queryInterface(items.getFirst());
                    System.out.println("Attaching listener to this item: " + item.getSubject());
                    MailItemEventsListener.attachTo(item);
                }
                else
                {
                    System.out.println("Failed to attach the listener. Inbox folder is empty.");
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
        MailItemListener listener = new MailItemListener();
        listener.execute();
        System.out.println("Press 'Enter' to terminate example");
        System.in.read();
    }
}