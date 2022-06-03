package be.rx4.impax.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef.HWND;

import java.awt.*;

import static be.rx4.impax.utils.NativeGUITools.User32DLL.*;

public class NativeGUITools {
    private static final int MAX_TITLE_LENGTH = 1024;

    public static boolean isImpax(){
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        GetWindowTextW(GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);
        //System.out.println("Active window title: " + Native.toString(buffer));
        String widowName = Native.toString(buffer);
        Rectangle dim;
        try {
            int[] rect = WindowRectangleUtility.getRect(widowName);
            dim = new Rectangle(rect[0],rect[1],rect[2],rect[3]);
        } catch (WindowRectangleUtility.WindowNotFoundException e) {
            e.printStackTrace();
        } catch (WindowRectangleUtility.GetWindowRectException e) {
            e.printStackTrace();
        }

        if(widowName.contains("IMPAX 6")){
            return true;
        } else {
            return false;
        }
    }

    public static Rectangle getWindowRectangle(){
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        GetWindowTextW(GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);
        String widowName = Native.toString(buffer);

        System.out.println("CURRENTLY SELECTED SCREEN IS: "+widowName);
        try {
            int[] rect = WindowRectangleUtility.getRect(widowName);
            return new Rectangle(rect[0]+8,rect[1]+8,rect[2]-8,rect[3]-8);
        } catch (WindowRectangleUtility.WindowNotFoundException e) {
            e.printStackTrace();
        } catch (WindowRectangleUtility.GetWindowRectException e) {
            e.printStackTrace();
        } catch (Throwable ignore){

        }
        return null;
    }

    static class Psapi {
        static { Native.register("psapi"); }
        public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
    }

    static class Kernel32 {
        static { Native.register("kernel32"); }
        private static int processQueryInformation = 0x0400;
        private static int processVmRead = 0x0010;
        public static native int GetLastError();
        public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);

        public static int getProcessQueryInformation() {
            return processQueryInformation;
        }

        public static void setProcessQueryInformation(int processQueryInformation) {
            Kernel32.processQueryInformation = processQueryInformation;
        }

        public static int getProcessVmRead() {
            return processVmRead;
        }

        public static void setProcessVmRead(int processVmRead) {
            Kernel32.processVmRead = processVmRead;
        }
    }

    static class User32DLL {
        static { Native.register("user32"); }
        public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);
        public static native HWND GetForegroundWindow();
        public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
    }
}