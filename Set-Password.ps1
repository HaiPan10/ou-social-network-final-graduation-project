param(
    [Parameter(Mandatory, Position=0, HelpMessage="Enter name")]
    [string]$Name
)

$Folder = "social_network"
$Resource = "src\main\resources"
$FileName = "application.yml"

$Account = ".\$Folder\account_service\$Resource\$FileName"
$Comment = ".\$Folder\comment_service\$Resource\$FileName"
$Post = ".\$Folder\post_service\$Resource\$FileName"

# 1 tab = 2 space
$Tab = "  "

$JsonObject = Get-Content .\config.json | ConvertFrom-Json

if($JsonObject.$Name){
    Write-Host -F Green "Starting to get all the item"
    $ListItem = Get-Item -Path $Account, $Comment, $Post
    $Pattern = "$Tab$Tab`password: .*"
    $New = "$Tab$Tab`password: $($JsonObject.$Name.password)"
    foreach($Item in $ListItem){
        $Content = Get-Content $Item
        if($Content | Select-String $Pattern){
            $Content -replace $Pattern, $New | Set-Content $Item.FullName
            Write-Host -F Green "Replace password in location `n$($Item.DirectoryName) successfully"
        }
    }

} else {
    Write-Host -F Red "Don't existed the name in config setting"
}