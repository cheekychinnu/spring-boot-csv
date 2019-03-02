package com.foo.csv;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/rest")
public class PersonController {

    @Value("${sampleFile}")
    private Resource sampleFile;
    private CsvMapper mapper = new CsvMapper();

    @RequestMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<List<Person>> uploadCSV(@RequestPart("file") MultipartFile file) throws IOException {
        List<Person> persons = read(Person.class, file.getInputStream());
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // Example when you need to directly write to the stream without creating a file.
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadCSV(HttpServletResponse response) throws IOException {
        Person p1 = new Person("Hank Reardon", 47, "California");
        Person p2 = new Person("Dagney Taggart", 38, "NewYork");
        List<Person> inputs = Stream.of(p1, p2).collect(Collectors.toList());
        write(Person.class, inputs, response);
    }

    // Example when you need to download from resource
    @RequestMapping(value = "/downloadSample", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadSampleCSV() throws IOException {

        return ResponseEntity.ok().contentLength(sampleFile.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + sampleFile.getFile().getName())
                .contentType(MediaType.parseMediaType(Files.probeContentType(sampleFile.getFile().toPath())))
                .body(sampleFile);
    }


    private <T> void write(Class<T> clazz, List<T> inputs, HttpServletResponse response) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnSeparator(',').withColumnReordering(false);
        ObjectWriter writer = mapper.writer(schema);
        ServletOutputStream outputStream = response.getOutputStream();


        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + "sample.csv");
        writer.writeValue(outputStream, inputs);
        response.flushBuffer();
    }

    private <T> List<T> read(Class<T> clazz, InputStream inputStream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        return reader.<T>readValues(inputStream).readAll();
    }
}
