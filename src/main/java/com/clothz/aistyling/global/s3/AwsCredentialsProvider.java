package com.clothz.aistyling.global.s3;

import software.amazon.awssdk.auth.credentials.AwsCredentials;

public interface AwsCredentialsProvider {
    AwsCredentials resolveCredentials();
}