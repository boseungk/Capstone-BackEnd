package com.clothz.aistyling.global.aws;

import software.amazon.awssdk.auth.credentials.AwsCredentials;

public interface AwsCredentialsProvider {
    AwsCredentials resolveCredentials();
}