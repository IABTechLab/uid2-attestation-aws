use jni::JNIEnv;
use jni::objects::JClass;
use jni::sys::{jbyteArray, jint, jbyte};
use nsm_io::{Request, Response, ErrorCode};
use serde_bytes::ByteBuf;

#[no_mangle]
#[allow(non_snake_case)]
#[allow(unused)]
pub extern "system" fn Java_com_uid2_attestation_aws_NitroAttestation_generateAttestationRequestInternal(
    env: JNIEnv,
    class: JClass,
    user_data: jbyteArray,
    public_key: jbyteArray,
    nonce: jbyteArray,
    buffer: jbyteArray,
) -> jint {
    let nsm = Nsm::connect();
    let params = AttestationParams {
        user_data: to_optional_array(user_data, &env),
        public_key: to_optional_array(public_key, &env),
        nonce: to_optional_array(nonce, &env),
    };
    match nsm.generate_attestation(params) {
        Ok(bytes) => {
            let len = bytes.len();
            match env.set_byte_array_region(buffer, 0, bytes.into_iter().map(|x| x as jbyte).collect::<Vec<jbyte>>().as_slice()) {
                Ok(()) => len as jint,
                Err(e) => -9 as jint, // java error
            }
        },
        Err(e) => e as jint,
    }
}

struct AttestationParams {
    user_data: Option<Vec<u8>>,
    nonce: Option<Vec<u8>>,
    public_key: Option<Vec<u8>>,
}

struct Nsm {
    fd: i32
}

impl Drop for Nsm {
    fn drop(&mut self) {
        nsm_driver::nsm_exit(self.fd);
    }
}

impl Nsm {
    pub fn connect() -> Self {
        Self {
            fd: nsm_driver::nsm_init(),
        }
    }

    pub fn generate_attestation(&self, params: AttestationParams) -> Result<Vec<u8>, i32> {
        match nsm_driver::nsm_process_request(self.fd, Request::Attestation {
            user_data: params.user_data.map(ByteBuf::from),
            nonce: params.nonce.map(ByteBuf::from),
            public_key: params.public_key.map(ByteBuf::from),
        }) {
            Response::Attestation { document } => Ok( document ),
            Response::Error (code) => Err(nsm_error_to_i32(code)),
            _ => Err(nsm_error_to_i32(ErrorCode::InvalidResponse))
        }
    }
}

fn to_optional_array(arr: jbyteArray, env: &JNIEnv) -> Option<Vec<u8>> {
    match env.convert_byte_array(arr) {
        Ok(bytes) => Some(bytes),
        _ => None
    }
}

fn nsm_error_to_i32(ec: ErrorCode) -> i32 {
    match ec {
        nsm_io::ErrorCode::Success => 0,
        nsm_io::ErrorCode::InvalidArgument => -1,
        nsm_io::ErrorCode::InvalidIndex => -2,
        nsm_io::ErrorCode::InvalidResponse => -3,
        nsm_io::ErrorCode::ReadOnlyIndex => -4,
        nsm_io::ErrorCode::InvalidOperation => -5,
        nsm_io::ErrorCode::BufferTooSmall => -6,
        nsm_io::ErrorCode::InputTooLarge => -7,
        nsm_io::ErrorCode::InternalError => -8,
    }
}