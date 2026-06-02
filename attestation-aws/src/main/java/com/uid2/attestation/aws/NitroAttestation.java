package com.uid2.attestation.aws;

public class NitroAttestation {
    
    public static NitroAttestationRequest generateAttestationRequest(NitroAttestationParams params) throws NitroException {
        byte[] buffer = new byte[20000];
        int result = generateAttestationRequestInternal(
            params.userData, 
            params.publicKey, 
            params.nonce, 
            buffer);
        
        if(result > 0) {
            return new NitroAttestationRequest(buffer, result);
        } else {
            throw NitroException.fromErrorCode(result);
        }
    }

    static {
        try {
            System.loadLibrary("jnsm");
        } catch (UnsatisfiedLinkError ignored) {}
    }

    private static native int generateAttestationRequestInternal(
        byte[] userData,
        byte[] publicKey,
        byte[] nonce,
        byte[] buffer);
}