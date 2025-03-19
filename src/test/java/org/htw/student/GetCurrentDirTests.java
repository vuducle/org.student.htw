package org.htw.student;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCurrentDirTests
{
    private static final Logger log = LoggerFactory.getLogger(GetCurrentDirTests.class);
    @Test
    public void testCurrentDirectory()
    {
        var userDirString = new FileSystemResource("").getFile().getAbsolutePath();
        log.info(userDirString);
    }
}
