
package changetextindocxfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FileHelper {

    //ТЕСТ создание пустого файла
    public void createEmptyFile(String fileName) throws IOException{
        File file = new File(fileName);
        //create the file.
        if (file.createNewFile()){
            System.out.println("File is created!");
        }
        else{
            System.out.println("File already exists.");
        }
        //write content
        FileWriter writer = new FileWriter (file);
        writer.write("Test data");
        writer.close();
    }
    
    //копируем файл
    public void copyFile(String from, String to) throws IOException{
        Path fromFile = Paths.get(from);
        Path toFile = Paths.get(to);

        // if fromFile doesn't exist, Files.copy throws NoSuchFileException
        if (Files.notExists(fromFile)) {
            System.out.println("File doesn't exist? " + fromFile);
            return;
        }

        // if toFile folder doesn't exist, Files.copy throws NoSuchFileException
        // if toFile parent folder doesn't exist, create it.
        Path parent = toFile.getParent();
        if(parent!=null){
            if(Files.notExists(parent)){
                Files.createDirectories(parent);
            }
        }

        
        if(Files.exists(parent, LinkOption.NOFOLLOW_LINKS)){
            // if toFile exist, replace it.
            Files.copy(fromFile, toFile, StandardCopyOption.REPLACE_EXISTING);
        } else {
            // default - if toFile exist, throws FileAlreadyExistsException
            Files.copy(fromFile, toFile);
            
        }

        // multiple StandardCopyOption
        /*CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES,
                LinkOption.NOFOLLOW_LINKS };

        Files.copy(fromFile, toFile, options);*/
        
    }

    //Меняет в файле fileName текст в соответствии с Map replaceString
    public void replaceStringsInFile(String fileName, Map<String, String> replaceStrings) throws IOException{
        Charset charset = StandardCharsets.UTF_8;
        Path path = Paths.get(fileName);
        
        for(Map.Entry<String, String> pair: replaceStrings.entrySet()){
            Files.write(path, new String(Files.readAllBytes(path), charset).replace(pair.getKey(), pair.getValue()).getBytes(charset));        
        }
    }
    
    //Заменяем в архиве zipFile файл fileToInZip файлом fileFrom
    public void replaceFileInZip(String zipFile, String fileFrom, String fileToInZip) throws IOException{
        Map<String, String> env = new HashMap<>(); 
        env.put("create", "true");
        Path path = Paths.get(zipFile);
        URI uri = URI.create("jar:" + path.toUri());
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            Path ff = Paths.get(fileFrom);
            Path nf = fs.getPath(fileToInZip);
            Files.copy(ff, nf, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    
    //Переименовываем файл
    public void renameFile(String oldFileName, String newFileName) throws IOException{
        Path oldName = Paths.get(oldFileName);
        Path newName = Paths.get(newFileName);
        
        Files.move(oldName, newName, StandardCopyOption.REPLACE_EXISTING);
    }
    
    
    //Удаление файла
    public void deleteFile(String fileName) throws IOException{
        Files.delete(Paths.get(fileName));
    }
    
    //ТЕСТ
    public void testTest() throws IOException{
        Map<String, String> env = new HashMap<>(); 
        env.put("create", "true");
        Path path = Paths.get("test.zip");
        URI uri = URI.create("jar:" + path.toUri());
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            Path nf = fs.getPath("word//new.txt");
            try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                writer.write("fucking hello");
            }
        }
    }
    
}
