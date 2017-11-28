# swagger-diff

This java jar provides a CLI to diff between two swagger api documents. It accepts three arguments

1. -c The current swagger (typically found in CI or on your local machine)
2. -d The deployed swagger (typically found in a deployed environment, this version is hardened)
3. -r The rules (Json file that can change the default rules)

## The Current Rules

| Java Class Name | Json Name | isBreakingChange |
|---|---|---|
| AddDefinition | add_definition | false |
| AddDescription | add_description  | false |
| AddMethod | add_method  | false |
| AddOptionalObjectProperty | add_optional_object_property | false |
| AddOptionalParam | add_optional_param | false |
| AddMethod | add_method | false |
| AddOptionalObjectProperty | add_optional_object_property | false |
| AddPath | add_path | false |
| AddRequiredParam | add_required_param | true |
| DeleteDefinition | delete_definition | true |
| DeleteMethod | delete_method | true |
| DeleteObjectProperty | delete_object_property | true |
| DeleteOperation | delete_operation | true |
| DeleteParam | delete_param | false|
| DeletePath | delete_path | true |
| DeleteProduce | delete_produce | true |
| DeleteResponse | delete_response | true |
| EditBasePath | edit_base_path | false |
| EditDescription | edit_description | false |
| EditHost | edit_host | false |
| EditOperationId | edit_operation_id | true |
| EditParamCollectionFormat | edit_param_collection_format | true |
| EditParamIn | edit_param_in | true |
| EditParamRequired | edit_param_required | true |
| EditParamType | edit_param_type | true |
| EditResponse | edit_response | true |
| EditSummary | edit_summary | false |