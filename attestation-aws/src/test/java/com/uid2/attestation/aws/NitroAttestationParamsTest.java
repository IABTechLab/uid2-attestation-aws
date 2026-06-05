package com.uid2.attestation.aws;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class NitroAttestationParamsTest {
    @Test
    public void testConstructorAssignsFields() {
        byte[] userData = {1, 2, 3};
        byte[] publicKey = {4, 5};
        byte[] nonce = {6};
        NitroAttestationParams p = new NitroAttestationParams(userData, publicKey, nonce);
        assertArrayEquals(userData, p.userData);
        assertArrayEquals(publicKey, p.publicKey);
        assertArrayEquals(nonce, p.nonce);
    }

    @Test
    public void testNullNonceAllowed() {
        NitroAttestationParams p = new NitroAttestationParams(new byte[]{1}, new byte[]{2}, null);
        assertNull(p.nonce);
    }
}
