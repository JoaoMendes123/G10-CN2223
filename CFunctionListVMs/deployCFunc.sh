#!/bin/bash
gcloud functions deploy getRunningVms --project=cn2223-t1-g10 --region=europe-west1 --entry-point=CfunctionVmsIP --runtime=java11 --trigger-http --allow-unauthenticated --source=target/deployment --service-account=service-acc-tf-g10@cn2223-t1-g10.iam.gserviceaccount.com --max-instances=3
