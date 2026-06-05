package com.uid2.attestation.aws;

import com.uid2.enclave.AttestationException;
import org.junit.Test;
import org.mockito.MockedStatic;

import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

public class NitroAttestationProviderTest {

    @Test
    public void testReturnsSlicedBuffer() throws AttestationException {
        byte[] fullBuffer = new byte[20000];
        fullBuffer[0] = 1; fullBuffer[1] = 2; fullBuffer[2] = 3;
        NitroAttestationRequest stub = new NitroAttestationRequest(fullBuffer, 3);

        try (MockedStatic<NitroAttestation> mocked = mockStatic(NitroAttestation.class)) {
            ArgumentCaptor<NitroAttestationParams> captor = ArgumentCaptor.forClass(NitroAttestationParams.class);
            mocked.when(() -> NitroAttestation.generateAttestationRequest(captor.capture())).thenReturn(stub);

            byte[] result = new NitroAttestationProvider()
                    .getAttestationRequest(new byte[]{9}, new byte[]{8});

            assertArrayEquals(new byte[]{1, 2, 3}, result);
            assertArrayEquals(new byte[]{9}, captor.getValue().publicKey);
            assertArrayEquals(new byte[]{8}, captor.getValue().userData);
            assertNull(captor.getValue().nonce);
        }
    }

    @Test
    public void testWrapsNitroExceptionAsAttestationException() {
        try (MockedStatic<NitroAttestation> mocked = mockStatic(NitroAttestation.class)) {
            NitroException root = NitroException.fromErrorCode(-1);
            mocked.when(() -> NitroAttestation.generateAttestationRequest(any())).thenThrow(root);

            try {
                new NitroAttestationProvider().getAttestationRequest(new byte[0], new byte[0]);
                fail("expected AttestationException");
            } catch (AttestationException e) {
                assertSame(root, e.getCause());
            }
        }
    }

    @Test
    public void testIsReadyDefaultTrue() {
        // NitroAttestationProvider delegates to IAttestationProvider.isReady() default (always true)
        assertTrue(new NitroAttestationProvider().isReady());
    }
}
