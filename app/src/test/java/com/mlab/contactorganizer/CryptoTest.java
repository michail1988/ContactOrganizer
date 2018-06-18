package com.mlab.contactorganizer;

import android.util.Base64;

import com.mlab.contactorganizer.utils.Crypto;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyByte;

public class CryptoTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void encode_equals_encode() throws Exception {
        String password = "Tojest moje hasełko.";

        assertEquals(Crypto.encode(password), Crypto.encode(password));
    }

    @Test
    public void encodeDecode() throws Exception {
       // PowerMockito.mockStatic(Base64.class);
        //PowerMockito.when(Base64.encode(any(), anyInt())).thenAnswer(invocation -> Base64.encode((byte[]) invocation.getArguments()[0]));
        //PowerMockito.when(Base64.decode(anyString(), anyInt())).thenAnswer(invocation -> java.util.Base64.getMimeDecoder().decode((String) invocation.getArguments()[0]));

        String password = "Tojest moje hasełko.";

        assertEquals(password, Crypto.decode(Crypto.encode(password)));
    }
}