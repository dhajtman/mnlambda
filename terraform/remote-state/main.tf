# This Terraform configuration file sets up a remote state backend using AWS S3.

# It specifies the AWS provider and the region to be used for resource creation.
provider "aws" {
  region = var.aws_region
}

# Create an S3 bucket for Terraform backend
resource "aws_s3_bucket" "terraform_backend" {
  bucket = var.backend_bucket_name
}