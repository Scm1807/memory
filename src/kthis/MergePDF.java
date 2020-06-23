package kthis;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

public class MergePDF {

    private static String[] getFiles(String folderPath) throws IOException {
        File folderFile = new File(folderPath);
        if (folderFile.isDirectory()) {
            return folderFile.list();
        }
        throw new IOException("文件夹路径错误！");
    }

    public static void main(String[] args) throws IOException, COSVisitorException {
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        String folderPath = "C:\\Users\\ihis\\Desktop\\新建文件夹 (2)";

        System.out.println("PDF文件夹路径: " + folderPath);
        String pathFileName = "C:\\Users\\ihis\\Desktop\\合成.pdf";

        String[] filesInFolder = getFiles(folderPath);
        for (int i = 0; i < filesInFolder.length; i++) {
            if (!filesInFolder[i].toLowerCase().endsWith(".pdf")) {
                continue;
            }
            mergePdf.addSource(folderPath + File.separator + filesInFolder[i]);
        }


        mergePdf.setDestinationFileName(pathFileName);
        mergePdf.mergeDocuments();
        System.out.print("合并完成");

        runFile(pathFileName);
    }

    public static void runFile(String filePath) {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("rundll32 url.dll FileProtocolHandler \"" + filePath + "\"");
        } catch (Exception e) {

        }
    }
}
