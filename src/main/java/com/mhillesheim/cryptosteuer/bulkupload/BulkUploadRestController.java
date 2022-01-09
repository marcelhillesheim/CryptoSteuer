package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.tradingplatform.TradingPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.time.format.DateTimeParseException;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping(path = "api/v1/bulk_upload")
public class BulkUploadRestController {
    private final BulkUploadService bulkUploadService;

    @Autowired
    public BulkUploadRestController(BulkUploadService bulkUploadService) {
        this.bulkUploadService = bulkUploadService;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    public void newBulkUpload(@RequestParam("file") MultipartFile file, @RequestParam("trading_platform") @Valid TradingPlatform tradingPlatform) {
        try {
            bulkUploadService.process(file, tradingPlatform);
            //TODO add additional catches for invalid data etc.
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "import failed", e);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "import failed. The file processor couldn't handle the given file", e);
        }
    }
}