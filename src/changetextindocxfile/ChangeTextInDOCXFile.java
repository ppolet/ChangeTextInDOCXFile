/*
Создает файл output//somename.docx из файла шаблона docx_template//template.docx.zip
где меняет специальные текстовые слова (типа {$text1}) на слова или предложения записанные в Map replaceStrings


1. Создать копию файла /docx_template/template.docx.zip в папке /output
2. Создать копию файла /docx_template/document.xml в /output/document.xml
3. изменить в нем {$text1} и {$text2} на желаемые строки
4. перезаписать файл /output/document.xml в архив-копию /output/template.docx.zip
5. удалить /output/document.xml
6. переименовать /output/template.docx.zip в /output/имяфайла.docx
7. закрыть открытые файлы
 */
package changetextindocxfile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeTextInDOCXFile {

    public static void main(String[] args) {
        FileHelper fileHelper = new FileHelper();
        
        //переменные для файлов
        String docxZipTempl;
        String outDocxZipTempl;
        String docXMLTempl;
        String outDocXML;
        String docXMLInZip;
        String outNameDocx;
        
        //1.
        docxZipTempl = "docx_template//template.docx.zip";
        outDocxZipTempl = "output//template.docx.zip";
        try {
            fileHelper.copyFile(docxZipTempl, outDocxZipTempl);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        //2.
        docXMLTempl = "docx_template//document.xml";
        outDocXML = "output//document.xml";
        try {
            fileHelper.copyFile(docXMLTempl, outDocXML);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        //Map с данными для замены в файле document.xml
        Map replaceStrings = new HashMap();
        replaceStrings.put("{$text1}", "Иванов Сидор Петрович");
        replaceStrings.put("{$text2}", "Генеральный уборщик 1 категории");
        
        //3.
        try {
            fileHelper.replaceStringsInFile(outDocXML, replaceStrings);
        } catch (IOException ex) {
            System.out.println(ex);
        }

        //4.
        docXMLInZip = "word//document.xml";
        try {
            fileHelper.replaceFileInZip(outDocxZipTempl, outDocXML, docXMLInZip);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        //5.
        try {
            fileHelper.deleteFile(outDocXML);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        //6.
        outNameDocx = "output//somename.docx";
        try {
            fileHelper.renameFile(outDocxZipTempl, outNameDocx);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
}
