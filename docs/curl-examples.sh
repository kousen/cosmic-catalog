#!/bin/bash
# Curl Examples for Cosmic Catalog API
# Run the application first: ./gradlew bootRun

BASE_URL="http://localhost:8080"

echo "=== Health Check ==="
curl -s "$BASE_URL/health" | jq '.'

echo -e "\n=== Get All Observations (Paginated) ==="
curl -s "$BASE_URL/api/observations?page=0&size=5" | jq '.'

echo -e "\n=== Get Featured Observations ==="
curl -s "$BASE_URL/api/featured?limit=3" | jq '.'

echo -e "\n=== Import Sample Data ==="
curl -X POST -s "$BASE_URL/api/import/sample" | jq '.'

echo -e "\n=== Approve an Observation (replace {id} with actual ID) ==="
# Example: Approve observation with ID 1
# curl -X POST -s "$BASE_URL/api/observations/1/approve" | jq '.'

echo -e "\n=== Approve with Optimistic Locking ==="
# Example: Approve with expected version
# curl -X POST -s "$BASE_URL/api/observations/1/approve?expectedVersion=0" | jq '.'

echo -e "\n=== Test Pagination ==="
curl -s "$BASE_URL/api/observations?page=0&size=2&sort=score,desc" | jq '.'

echo -e "\n=== Test Featured with Different Limits ==="
curl -s "$BASE_URL/api/featured?limit=1" | jq '.[] | {id, telescope, targetName, score}'
curl -s "$BASE_URL/api/featured?limit=10" | jq 'length'

echo -e "\n=== Test Error Handling ==="
echo "Fetching non-existent observation (should return 404):"
curl -s -o /dev/null -w "%{http_code}\n" "$BASE_URL/api/observations/99999/approve"

echo -e "\n=== Test Validation Error ==="
echo "Fetching featured with invalid limit (should return 400):"
curl -s -o /dev/null -w "%{http_code}\n" "$BASE_URL/api/featured?limit=0"
curl -s -o /dev/null -w "%{http_code}\n" "$BASE_URL/api/featured?limit=101"

echo -e "\n=== Done! ==="