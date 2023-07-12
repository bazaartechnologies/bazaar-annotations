package com.tech.bazaar.ksp.repository

import com.tech.bazaar.ksp.lds.ILDS
import com.tech.bazaar.ksp.rds.IRDS

class RepositoryImpl(val rds: IRDS, val lds: ILDS) : IRepository