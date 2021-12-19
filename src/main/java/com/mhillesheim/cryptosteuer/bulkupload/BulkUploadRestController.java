package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/bulk_upload")
public class BulkUploadRestController {
    private final BulkUploadService bulkUploadService;

    @Autowired
    public BulkUploadRestController(BulkUploadService bulkUploadService) {
        this.bulkUploadService = bulkUploadService;
    }


    //TODO check if @Valid is correctly implemented
    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    public void newBulkUpload(@RequestParam("file") MultipartFile file, @RequestParam("trading_platform") @Valid TradingPlatform tradingPlatform) {
        //TODO handle exception
        try {
            bulkUploadService.process(file, tradingPlatform);
            //TODO add additional catches for invalid data etc.
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "import failed", e);
        }
    }
}