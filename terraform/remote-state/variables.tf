variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "us-east-1"
}

variable "backend_bucket_name" {
  type        = string
  description = "The S3 bucket name for the Terraform backend"
  default = "entsoe-backend-bucket" # Replace with your actual bucket name
}