import com.l9e.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

/**
 * file oper
 *
 * @author meizs
 * @create 2018-03-09 16:31
 **/
public class FileOper {
    public static void main(String[] args) {

        FileUtil.writeFile("file/station_name.js1", "", "utf-8");

        try {
            FileReader fileReader = new FileReader("file/station_name_" + new Date() + ".csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
