package com.weavernorth.datadeal.toread.manage.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther Lylong
 * @Date 2019/4/26 10:21
 */
@Component
public class WriteExceptionLogUtil
{
    public static Log log = LogFactory.getLog(WriteExceptionLogUtil.class.getName());

    public static  void writeLog(String msg)
    {
        msg = msg+"\n";
        FileOutputStream fos = null;
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd :HH:MM:ss");
        String dateString = df.format(new Date());
        msg = dateString+": "+msg;
        dateString = dateString.substring(0,10);
        String logName = dateString+"_log.txt";
        String path=System.getProperty("user.dir")+"/logs/exceptionlog/"+logName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try
        {
            fos = new FileOutputStream(path,true);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            log.error("日志记录失败:"+e.getMessage());
        }
        finally
        {
            //true表示在文件末尾追加
            try
            {
                fos.write(msg.getBytes());
                fos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
                log.error("日志记录失败:"+e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd :HH:MM:ss");
        String dateString = df.format(new Date()).substring(0,10);
        System.out.println(dateString);
//        System.out.println(System.getProperty("user.dir"));
//        writeLog("2019-4-26:xxxxxxxxxxxxxxxxx");

    }
}
