package kthis;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

@SuppressWarnings({"rawtypes"})
public class SvnUtil {

    private static String name = "sucongming"; // 姓名
    private static String password = "scm"; // 密码
    private static String fileFolder = "C:\\KarryTech\\svn\\"; // 检出地址
    private static String url = "svn://work.karrytech.com:2902/leopard/branches/dev"; // svn库地址
    private static int count = 1; // 检出文件数量
    private static SVNURL repositoryURL = null;
    private static SVNRepository repository = null;
    private static SVNUpdateClient updateClient = null;

    /**
     * Description: 检出svn库相关版本文件到指定文件地址
     *
     * @param versionList
     * @author SCM 2020-4-16
     */
    public static void checkOut(List<Integer> versionList) {
        Calendar ca = Calendar.getInstance();
        StringBuffer year = new StringBuffer("" + ca.get(Calendar.YEAR) + (ca.get(Calendar.MONTH) + 1) + ca.get(Calendar.DATE));
        File wcDir = new File(fileFolder + year);
        if (wcDir.exists())
            wcDir.delete();

        // 判断传入版本号
        if (versionList == null || versionList.size() == 0) {
            System.out.println("版本号不能为空！");
        } else {
            // 首先处理版本号，版本号正序排序
            for (int i = 0; i < versionList.size() - 1; i++) {
                for (int j = 1; j < versionList.size() - i; j++) {
                    if ((versionList.get(j - 1)).compareTo(versionList.get(j)) > 0) { // 比较两个整数的大小
                        Integer a = versionList.get(j - 1);
                        versionList.set((j - 1), versionList.get(j));
                        versionList.set(j, a);
                    }
                }
            }
            try {
                // 设置库属性
                setupLibrary();
                // 循环下载所有添加的版本号下的文件
                for (int i = 0; i < versionList.size(); i++) {
                    listFiles("", versionList.get(i), year.toString());
                }
            } catch (SVNException svne) {
                svne.printStackTrace();
                System.err.println("创建版本库实例时失败，版本库的URL是 : " + svne.getMessage());
                System.exit(1);
            }

            try {
                if (count == 1) {
                    System.out.println("\n未查到相关文件！");
                }
                System.out.println("\n最新版本号:" + (repository.getLatestRevision() - 1));
            } catch (SVNException svne) {
                System.err.println("获取最新版本号时出错: " + svne.getMessage());
                System.exit(1);
            }
        }
    }

    private static void setupLibrary() throws SVNException {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();

        repositoryURL = SVNURL.parseURIEncoded(url);
        repository = SVNRepositoryFactory.create(repositoryURL);

        // 实例化客户端管理类
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, name, password);

        // 通过客户端管理类获得updateClient类的实例。
        updateClient = ourClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
    }

    /*
     * 此函数递归的获取版本库中某一目录下的所有条目。
     */
    private static void listFiles(String path, int version, String year) throws SVNException {
        // 获取版本库的path目录下的所有条目。参数-1表示是最新版本。
        Collection entries = repository.getDir(path, version, null, (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            if (entry.getRevision() == version) {
                // 如果是文件直接检出
                if (entry.getKind() == SVNNodeKind.FILE) {
//					try {
                    System.out.println("\n" + count++ + "、/" + (path.equals("") ? "" : path + "/") + entry.getName());
//						SVNURL url = SVNURL.parseURIEncoded(repositoryURL + "/" + path + "/" + entry.getName());
//						File file = new File(fileFolder + year + "/" + path + "/" + entry.getName());
//						long workingVersion = updateClient.doExport(url, file, SVNRevision.HEAD, SVNRevision.parse(version + ""), "downloadModel",
//								true, false);
//						System.out.println(count++ + "、版本：" + workingVersion + " 检出地址：" + file);
//					} catch (SVNException e) {
//						System.out.println(count++ + "、检出失败，该文件已被删除！");
//					}
                }
                // 如果为目录递归执行
                if (entry.getKind() == SVNNodeKind.DIR) {
                    listFiles((path.equals("")) ? entry.getName() : path + "/" + entry.getName(), version, year);
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> versionList = new ArrayList<>();
        versionList.add(76771);
        SvnUtil.checkOut(versionList);
    }

}