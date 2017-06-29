package lnyswz.jxc.util;

import java.io.File;

/**
 * Created by Wenyang on 2017/6/29.
 */
public class Upload {

    public static boolean deleteFile(String sPath) {
        File file = new File(Util.getRootPath() + sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
}
