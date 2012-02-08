/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.Date;
import com.jniwrapper.win32.ole.OleFunctions;

import java.util.Calendar;

import outlook.outlook.*;

/**
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Outlook 11.0 Object Library
 * ProgID:      Outlook.Application
 * GUID:        {00062FFF-0000-0000-C000-000000000046}
 * In the package: outlook
 *
 * You can generate stubs using the Code Generator application.
 */
public class Planner
{
    private _NameSpace _mapiNS;

    public static void main(String[] args)
    {
        new Planner().execute();
    }

    protected void doWork()
    {
        login();

        MAPIFolder taskFolder = getTaskFolder();
        createTask(taskFolder);

        MAPIFolder calendarFolder = getCalendarFolder();
        createAppointment(calendarFolder);
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

    private MAPIFolder getTaskFolder() throws ComException
    {
        MAPIFolder result = _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderTasks));
        return result;
    }

    private MAPIFolder getCalendarFolder() throws ComException
    {
        MAPIFolder result = _mapiNS.getDefaultFolder(new OlDefaultFolders(OlDefaultFolders.olFolderCalendar));
        return result;
    }

    private void createAppointment(MAPIFolder calendarFolder) throws ComException
    {
        final Variant itemType = new Variant(OlItemType.olAppointmentItem);
        final IDispatch appointment = calendarFolder.getItems().add(itemType);
        _AppointmentItem newAppt = AppointmentItem.queryInterface(appointment);

        //sep up some properties
        newAppt.setSubject(new BStr("Another Test"));
        newAppt.setBody(new BStr("Hello! Again!"));

        Calendar calendar = Calendar.getInstance();
        Date current = new Date(calendar.getTime());
        calendar.add(Calendar.MINUTE, 30);
        Date tmEnd = new Date(calendar.getTime());
        newAppt.setStart(current);
        newAppt.setEnd(tmEnd);

        newAppt.setAllDayEvent(new VariantBool(false));
        newAppt.setLocation(new BStr("LPB"));

        newAppt.save();
    }

    private void createTask(MAPIFolder tasksFolder) throws ComException
    {
        final Variant itemType = new Variant(OlItemType.olTaskItem);
        final IDispatch task = tasksFolder.getItems().add(itemType);
        _TaskItem newTask = TaskItem.queryInterface(task);

        //set up some properties for the new task
        newTask.setSubject(new BStr("This is a test"));
        newTask.setBody(new BStr("How are you doing today?"));

        Date current = new Date(new java.util.Date());
        newTask.setStartDate(current);
        newTask.setDueDate(current);

        newTask.save();
    }

    public final void execute()
    {
        init();

        int result = 0;
        try
        {
            doWork();
        }
        catch (ComException e)
        {
            e.printStackTrace();
            result = -1;
        }
        finally
        {
            shutdown();
        }

        System.exit(result);
    }

    private void init()
    {
        OleFunctions.oleInitialize();
    }

    private void shutdown()
    {
        OleFunctions.oleUninitialize();
    }
}