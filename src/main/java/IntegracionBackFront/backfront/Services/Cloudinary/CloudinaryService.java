package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;



@Service
public class CloudinaryService
{
    //Constante define el tamaño maximo permitido para los archivos
    private static final long MAX_FILE_SIZE = 5*1024*1024;

    //Constante para definir que archivos son admitidos
    private static final String[] ALLOWED_EXTENSIONS = {".jpg",".jpe",".png"};

    //Cliente de clouddinary inyectado como dependcia
    private Cloudinary cloudinary;

    /**
     *
     * @param cloudinary
     */
    public CloudinaryService(Cloudinary cloudinary)
    {
        this.cloudinary = cloudinary;
    }

    /**
     * sube la carpeta a imagen asi como sube una carpeta en especifico
     *
     * @param file
     * @return url de la imagen
     * @throws IOException
     */
    public String uploadimage(MultipartFile file) throws IOException
    {
        //1. Validamos el archivo
        validateImage(file);

        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto",
                "quality","auto:good"
        ));
        //Retornamos la url de la imagen
        return (String) uploadResult.get("secure_url");
    }

    /**
     *sube la carpeta a imagen asi como sube una carpeta en especifico
     * @param file
     * @param folder
     * @return URL segura (HTTPS) de la imagen subida
     * @throws IOException
     */
    public  String uploadimage(MultipartFile file, String folder) throws  IOException
    {
        validateImage(file);
        //Genera un nombre unico del archivo
        //Conservar la extension original
        //Agregra un prefijo y un UUID para evitar coliciones

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFilename = "img_"+ UUID.randomUUID() + fileExtension;

        //Configuracion para subir imagen
        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,         //Carpeta de Destino
                "public_id", uniqueFilename,      //Nombre unico para el archivo
                "use_filename", false,            //No usar el nombre original
                "unique_filename", false,         //No generar nombre unico
                "overwrite", false,               //No sobreeescribir archivos
                "resource_type", "auto",          //Auto detectar tipo de acceso
                "quality", "auto:good"            //Optimizacion de calidad automatica
        );
        //Subir el archivo
        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        //Retornamos la URL segura
        return (String) uploadResult.get("secure_url");
    }

    /**
     *
     * @param file
     */
    private void validateImage(MultipartFile file)
    {
        //1. Verificar si el archivo esta vacio
        if (file.isEmpty())
        {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }
        //2. Verifica rel tamaño de la imagen
        if (file.getSize() > MAX_FILE_SIZE)
        {
            throw new IllegalArgumentException("El archivo no puede ser mayor a 5mb");
        }

        //3. Obtener y validar el nombre general del archivo
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null)
        {
            throw  new IllegalArgumentException("Nombre del archivo invalido");
        }
        //4. Extraer y Validar
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWED_EXTENSIONS).contains(extension))
        {
            throw new  IllegalArgumentException("Solo sepermiten JPG, JPEG y PNG");
        }

        //5.Verificar el IMEdel archivo
        if(!file.getContentType().startsWith("image/"))
        {
            throw new IllegalArgumentException("El archivo debe ser una imagen valida");
        }
    }
}
