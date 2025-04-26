# Outputs for the Terraform remote state S3 bucket
output "terraform_backend_bucket_arn" {
  value = aws_s3_bucket.terraform_backend.arn
}