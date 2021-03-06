<#macro indicator icon status metric legend title="">
<div class="topic-abstract text-center">
<#if title?has_content> 
	<div>
    	<span>${title}</span>
    </div>
</#if>
    <div>
 	<span class="indicator text-<@classByStatus status/>">${metric}</span><span> ${legend}</span>
 	</div>
</div> 
</#macro>

<#macro card title >
<div class="card">
	<div class="card-header font-weight-bold">
	    <span>${title}</span>
	 </div>
	 <div class="card-body text-center">
	 	<#nested>
	 </div>
</div> 
</#macro>


<#macro health>
	<div class="row" >
		<#list features as feature>
			<div class="card" style="width: 20rem;">
			  <div class="card-block">
			    <h4 class="card-title">${feature}</h4>
			    <ul class="list-group">
			    <#list healthchecksByFeature[feature] as healthcheck>
					<li class="list-group-item list-group-item-<#switch healthcheck.measure.status >
							<#case 'GREEN'>success<#break>
							<#case 'YELLOW'>warning<#break>
							<#case 'RED'>danger<#break>
							<#default>
						</#switch>" >
						<div class="container" >
							<span>${healthcheck.name}</span>
							<button type="button" class="btn btn-secondary float-right" data-toggle="popover"  data-html="true"  data-content="
														<ul>
															<li>checkedBy : ${healthcheck.checker}</li>
															<li>at : ${healthcheck.checkInstant}</li>
														</ul>">Detail</button>
						</div>
					</li>
				</#list>
				</ul>
			  </div>
			</div>
		</#list>
	</div>
</#macro>


<#macro standardPanel title feature>
<div class="card mx-3 mb-3">
	<div class="card-header">
		${title}
		<@healthByFeature feature />
	</div>
	<div class="card-body">
		<div class="row">
		<#nested>
		</div>
	</div>
</div>
</#macro>

<#macro standardList>
<div class="col-4 topic-panel">
	<div class="list-group" id="list-tab" role="tablist">
		<#nested>
	</div>
</div>
</#macro>


<#macro standardDetail>
<div class="col-8">
	<div class="tab-content" id="nav-tabContent">
		<#nested>
	</div>
</div>
</#macro>




<#macro healthByFeature feature >
	<#if healthchecksByFeature[feature]?? >
		<#list healthchecksByFeature[feature] as healthCheck>
			<span class="badge badge-<#switch healthCheck.measure.status >
						<#case 'GREEN'>success<#break>
						<#case 'YELLOW'>warning<#break>
						<#case 'RED'>danger<#break>
						<#default>
					</#switch>" >${healthCheck.name}</span>
		</#list>
	</#if>
</#macro>

<#macro classByStatus status >
<#switch status >
	<#case 'GREEN'>success<#break>
	<#case 'YELLOW'>warning<#break>
	<#case 'RED'>danger<#break>
	<#default>
</#switch>
</#macro>

<#macro line type name active=''>
	<a class="list-group-item list-group-item-action" id="${type}Detail-${name}-list" data-toggle="list" href="#${type}Detail-${name}" role="tab">
		<span>${name}</span>
		<#if active?has_content>
		<i class="material-icons float-right">
			<#if active>
				done
			<#else> 
				error
			</#if>
		</i>
		</#if>
	</a>
</#macro>

<#macro lineDetail type name>
<div class="tab-pane fade" id="${type}Detail-${name}" role="tabpanel">
<#nested>
</div>
</#macro>

<#macro property label value=''>
<label class="col-sm-2 col-form-label">${label}</label>
<div class="col-sm-10">
  <p class="form-control-plaintext  font-weight-bold"><#if value?has_content >${value}<#else>N/A</#if></p>
</div>
</#macro>

<#macro formGroup>
<div class="form-group row">
<#nested>
</div>
</#macro>